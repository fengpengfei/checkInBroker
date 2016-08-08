package com.fooluodi.broker.controllers;

import com.fooluodi.broker.framework.ResponseEntity;
import com.fooluodi.broker.framework.WebAPIBaseController;
import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by di on 8/8/2016.
 */
@RestController
@RequestMapping(value = "/cmd")
public class CmdController extends WebAPIBaseController {

    private static final Logger logger = LoggerFactory.getLogger(CmdController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    //, produces = "application/json;charset=UTF-8"
    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public ResponseEntity<?> addUser(String userName, String passwd, String session) {
        logger.info("add new user, userName:{}, passwd:{}, session:{}", userName, passwd, session);

        int id = userService.addUser(userName, passwd, session);

        logger.info("add user success, userId:{}", id);

        return ResponseEntity.success(id);
    }

    @RequestMapping(value = "/addAnonymousUser", method = RequestMethod.GET)
    public ResponseEntity<?> addAnonymousUser(String session) {
        logger.info("addAnonymousUser, session:{}", session);

        int id = userService.addUser(session);

        logger.info("add user success, userId:{}", id);

        return ResponseEntity.success(id);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> showUsers() {
        logger.info("show all users");

        List<UserInfoBo> allUsers = userService.getAllUsers();

        return ResponseEntity.success(allUsers);
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public ResponseEntity<?> showLogs() {
        logger.info("show all logs");

        List<LogBo> allLogs = logService.getAllLogs();

        logger.info("get sum:{}", allLogs.size());
        return ResponseEntity.success(allLogs);
    }

    @RequestMapping(value = "/logs/${type}", method = RequestMethod.GET)
    public ResponseEntity<?> showLogsByType(@PathVariable("type") int type) {
        logger.info("show logs by type:{}", type);

        List<LogBo> allLogs = logService.getLogsByType(type);

        logger.info("get sum:{}", allLogs.size());
        return ResponseEntity.success(allLogs);
    }
}
