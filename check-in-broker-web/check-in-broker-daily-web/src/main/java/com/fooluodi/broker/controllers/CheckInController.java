package com.fooluodi.broker.controllers;

import com.fooluodi.broker.checkin.CheckInService;
import com.fooluodi.broker.framework.ResponseEntity;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by di on 24/8/2016.
 */

@RestController
@RequestMapping(value = "/checkin")
public class CheckInController {
    private static final Logger logger = LoggerFactory.getLogger(CheckInController.class);

    @Resource
    private CheckInService checkInService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> checkIn4User(@PathVariable("userId") int userId) {
        logger.info("check in for userId:{}", userId);


        return ResponseEntity.success(null);
    }
}
