package com.fooluodi.broker.controllers;

import com.fooluodi.broker.framework.ResponseEntity;
import com.fooluodi.broker.framework.WebAPIBaseController;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    //, produces = "application/json;charset=UTF-8"
    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public ResponseEntity<?> addUser(String userName, String passwd){
        logger.info("add new user, userName:{}, passwd:{}", userName, passwd);

        int id = userService.addUser(userName, passwd);

        logger.info("add user success, userId:{}", id);

        return ResponseEntity.success(id);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> showUsers(){
        logger.info("show all users");

        List<UserInfoBo> allUsers = userService.getAllUsers();

        return ResponseEntity.success(allUsers);
    }
}
