package com.fooluodi.broker.checkin;

import com.fooluodi.broker.constant.Constant;
import com.fooluodi.broker.mail.service.MailService;
import com.fooluodi.broker.poi.bo.POI;
import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.constant.LogType;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.poi.service.PoiService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.service.UserService;
import com.fooluodi.broker.util.http.HttpResponseEntity;
import com.fooluodi.broker.util.json.JsonHelper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by di on 7/8/2016.
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    private static final Logger logger = LoggerFactory.getLogger(CheckInServiceImpl.class);

    private static final String CHECK_IN_URL = "http://emp.dper.com/checkin/mobile";

    private static final int REDIRECT_LIMIT_MAX = 5;

    private static final String ENCODE_UTF8 = "utf-8";

    @Resource
    private LogService logService;

    @Resource
    private PoiService poiService;

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    public static PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
    public static HttpClient client;

    static {
        connectionManager.setMaxTotal(1000);
        connectionManager.setDefaultMaxPerRoute(200);
        HttpParams my_httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(my_httpParams, 5000);
        HttpConnectionParams.setSoTimeout(my_httpParams, 50000);
        client = new DefaultHttpClient(connectionManager, my_httpParams);
    }

    public static final String ENCODE = "utf-8";

    @Override
    public boolean checkIn(UserInfoBo userInfoBo) {
        logger.info("user:{}, begin check in.", userInfoBo);

        boolean result = true;

        //entity
        POI poi = poiService.getRandomPoi();
        Assert.notNull(poi, "no valid poi!");
        logger.info("random a poi:{}", poi);

        StringBuilder logDetail = new StringBuilder();

        //header
        String session = userInfoBo.getValidSession().trim();
        Map<String, String> cookiesMap = transCookies(session);
        String cookieStr = this.map2CookieStr(cookiesMap);
        logger.info("cookies:{}", cookieStr);

        Map<String, String> defaultHeaders = this.getDefaultHeaders();

        defaultHeaders.put("Cookie", cookieStr);

        logDetail.append("check in for:").append(userInfoBo).append("\n");
        try {
            HttpResponseEntity responseEntity = doJsonPost(CHECK_IN_URL, JsonHelper.transObjToJsonString(poi), defaultHeaders, ENCODE_UTF8, REDIRECT_LIMIT_MAX);

            Assert.isTrue(responseEntity.getResponseCode() == 200, "failed!");

            logDetail.append("check in result:").append(responseEntity.getResponseCode()).append("\n")
                    .append(" result body:").append(responseEntity.getResponseContent()).append("\n");

        } catch (IOException e) {
            logger.error("check in error!", e);
            result = false;
        }

        this.saveOplog(userInfoBo.getId(), logDetail.toString(), result);

        this.handleUserResult(userInfoBo, poi, result);

        return result;
    }

    private void handleUserResult(UserInfoBo userInfoBo, POI poi, boolean result) {
        logger.info("handle user result");

        if (Objects.equals(userInfoBo.getMailNotify(), Constant.YES)) {
            StringBuilder body = new StringBuilder();

            body.append("<html>")
                    .append("hi ").append(userInfoBo.getUserName()).append(":<br><br>")
                    .append("打卡时间:").append(new Date()).append("<br>")
                    .append("打卡经纬度:").append(poi).append("<br>")
                    .append("打卡结果:").append(result).append("<br>")
                    .append("</html>");

            try {
                mailService.sendMail(userInfoBo.getMailAddress(), body.toString(), "打卡日报");
            } catch (Exception e){
                logger.error("report mail failed.", e);
            }
        }
    }

    @Override
    public boolean checkIn(int userId) {
        logger.info("check in for user:{}", userId);

        UserInfoBo userById = userService.getUserById(userId);

        logger.info("find user!user:{}", userById);
        return this.checkIn(userById);
    }

    private void saveOplog(int userId, String detail, boolean isSuccess) {
        LogBo log = new LogBo();

        log.setUserId(userId);
        log.setDetail(detail + "\nresult:" + isSuccess);
        log.setOpType(LogType.CHECK_IN);
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));

        logService.asyncAddLog(log);
    }

    private String map2CookieStr(Map<String, String> cookieMap) {

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(";");
        }
        stringBuilder.append("foo=foo");
        return stringBuilder.toString();
    }

    private HttpResponseEntity doJsonPost(String url, String jsonEntity, Map<String, String> headers, String encoding, int tryTimes) throws IOException {
        logger.info("going to post Url:{}, args:{}, tryTimes:{}", url, jsonEntity, tryTimes);
        if (tryTimes <= 0)
            return null;

        tryTimes--;

        HttpPost post = new HttpPost(url);

        //body
        StringEntity entity = new StringEntity(jsonEntity, encoding);
        entity.setContentType("application/json");

        post.setEntity(entity);

        //headers
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            post.addHeader(entry.getKey(), entry.getValue());
        }

        return exec(post, jsonEntity, headers, encoding, tryTimes);
    }

    private HttpResponseEntity exec(HttpPost post, String jsonEntity, Map<String, String> headers, String encoding, int tryTimes) throws IOException {
        HttpResponse execute = client.execute(post);

        logger.info("response code:{}", execute.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(execute.getEntity().getContent()));


        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            logger.info("response:{}", line);
            stringBuilder.append(line);
        }
        Header locationHeader = execute.getFirstHeader("location");
        // 返回代码为302,301时，表示页面己经重定向，则重新请求location的url。
        if (locationHeader != null) {
            String redirectUrl = locationHeader.getValue();
            return doJsonPost(redirectUrl, jsonEntity, headers, encoding, tryTimes);
        }

        HttpResponseEntity result = new HttpResponseEntity();
        result.setResponseCode(execute.getStatusLine().getStatusCode());
        result.setResponseContent(stringBuilder.toString());
        return result;
    }

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> map = new HashedMap();

        map.put("Host", "emp.dper.com");
        map.put("Content-Type", "application/json");
        map.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_0_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13A404 XM/4.3.1.317");
        map.put("Origin", "http://emp.dper.com");
        map.put("Referer", "http://emp.dper.com/checkin/");

        return map;
    }

    private final Set<String> needCookies = new HashSet<>(Arrays.asList(
            "_ele_uid",
            "JSESSIONID",
            "uid",
            "u",
            "ai",
            "appId",
            "deviceType",
            "dt"
    ));

    private Map<String, String> transCookies(String session) {
        logger.info("trans cookies session:{}", session);

        Map<String, String> cookieMap = new HashMap<>();

        String[] split = session.split(";");

        for (String s : split) {
            String[] split1 = s.split("=");
            if (split1.length != 2) continue;

            if (needCookies.contains(split1[0].trim())) {
                cookieMap.put(split1[0].trim(), split1[1].trim());
            }
        }
        return appendCookies(cookieMap);
    }

    private Map<String, String> appendCookies(Map<String, String> map) {
        //增加打卡时间
        Date now = new Date();
        map.put("date", now.getTime() + "");

        return map;
    }
}
