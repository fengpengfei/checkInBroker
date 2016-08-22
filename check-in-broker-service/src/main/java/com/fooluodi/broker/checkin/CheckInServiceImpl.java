package com.fooluodi.broker.checkin;

import com.fooluodi.broker.checkin.poi.POI;
import com.fooluodi.broker.checkin.poi.POIGen;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.util.http.HttpResponseEntity;
import com.fooluodi.broker.util.json.JsonHelper;
import com.fooluodi.broker.util.time.DateUtil;
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

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
    public static HttpClient client;

    static {
        connectionManager.setMaxTotal(1000);
        connectionManager.setDefaultMaxPerRoute(200);
        HttpParams my_httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(my_httpParams, 500000);
        HttpConnectionParams.setSoTimeout(my_httpParams, 5000000);
        client = new DefaultHttpClient(connectionManager, my_httpParams);
    }

    public static final String ENCODE = "utf-8";

    @Override
    public boolean checkIn(UserInfoBo userInfoBo) {
        logger.info("user:{}, begin check in.", userInfoBo);

        //entity
        POI poi = POIGen.randomPoi();
        logger.info("random a poi:{}", poi);

        //header
        String session = userInfoBo.getValidSession().trim();
        Map<String, String> cookiesMap = transCookies(session);
        String cookieStr = this.map2CookieStr(cookiesMap);
        logger.info("cookies:{}", cookieStr);

        Map<String, String> defaultHeaders = this.getDefaultHeaders();

        defaultHeaders.put("Cookie", cookieStr);

        try {
            HttpResponseEntity responseEntity = doJsonPost(CHECK_IN_URL, JsonHelper.transObjToJsonString(poi), defaultHeaders, ENCODE_UTF8, REDIRECT_LIMIT_MAX);

            System.out.println(responseEntity);

        } catch (IOException e) {
            logger.error("check in error!", e);
            return false;
        }
        return true;
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

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(execute.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        Header locationHeader = execute.getFirstHeader("location");
        // 返回代码为302,301时，表示页面己经重定向，则重新请求location的url。
        if (locationHeader != null) {
            String redirectUrl = locationHeader.getValue();
            return doJsonPost(redirectUrl, jsonEntity, headers, encoding, tryTimes);
        }

        HttpResponseEntity result = new HttpResponseEntity();
        result.setResponseCode(execute.getStatusLine().getStatusCode());
        result.setResponseContent(EntityUtils.toString(execute.getEntity()));
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
