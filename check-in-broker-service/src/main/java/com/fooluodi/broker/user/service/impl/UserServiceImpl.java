package com.fooluodi.broker.user.service.impl;

import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.dao.UserInfoMapper;
import com.fooluodi.broker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by di on 7/8/2016.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfoBo> getAllUsers() {

        return null;
    }

    @Override
    public List<UserInfoBo> getAllAnonymousUser() {
        return null;
    }

    @Override
    public List<UserInfoBo> getAllValidUser() {
        return null;
    }
}
