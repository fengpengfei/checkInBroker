package com.fooluodi.broker.worker;

import com.fooluodi.broker.checkin.CheckInService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by di on 23/8/2016.
 */
@Component
public class CheckInWorker {
    private static final Logger logger = LoggerFactory.getLogger(CheckInWorker.class);

    @Resource
    private CheckInService checkInService;

    private static final int DEFAULT_TRY_TIMES = 3;

    private static final int SLEEP_BASE_TIME_MIN = 5;
    private static final int SLEEP_RANDOM_LIMIT_TIME_MAX = 20;



    @Resource
    private UserService userService;

    //9, 16, 20, 22 点自动运行
    public void exec() {
        long start = System.currentTimeMillis();
        logger.info("worker begin!");

        List<UserInfoBo> allUsers = userService.getAllUsers();

        //filter
        List<UserInfoBo> needCheckInUsers = userService.filterUers(allUsers);

        logger.info("check in for users:{}"
                , needCheckInUsers.stream().map(userInfoBo -> userInfoBo.getId()).collect(Collectors.toList()));

        //休息随机时间, 5分钟 + 20分钟内随机时间
        Random random = new Random();
        int i = random.nextInt(SLEEP_RANDOM_LIMIT_TIME_MAX);

        try {
            logger.info("going to sleep:{} min.", SLEEP_BASE_TIME_MIN + i);
            Thread.sleep((SLEEP_BASE_TIME_MIN + i) * 60 * 1000);
        } catch (InterruptedException e) {
        }

        needCheckInUsers.stream().forEach(user -> {
            try {
                this.checkInWithTryTimes(user, DEFAULT_TRY_TIMES);
            } catch (Exception e) {
                logger.error("user:{}, check in failed!", user, e);
            }
        });

        logger.info("exec done!last:{} ms", System.currentTimeMillis() - start);
    }

    private boolean checkInWithTryTimes(UserInfoBo user, int tryTimes) {
        if (tryTimes <= 0)
            return false;
        return checkInService.checkIn(user) || checkInWithTryTimes(user, --tryTimes);
    }
}
