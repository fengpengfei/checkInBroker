package com.fooluodi.broker.worker;

import com.fooluodi.broker.checkin.CheckInService;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by di on 23/8/2016.
 */
@Component
public class CheckInWorker {
    private static final Logger logger = LoggerFactory.getLogger(CheckInWorker.class);

    @Resource
    private CheckInService checkInService;

    @Resource
    private UserService userService;

    public void exec() {
        long start = System.currentTimeMillis();
        logger.info("worker begin!");

    }
}
