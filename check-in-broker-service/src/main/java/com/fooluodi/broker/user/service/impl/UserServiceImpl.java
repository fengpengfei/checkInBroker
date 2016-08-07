package com.fooluodi.broker.user.service.impl;

import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.constant.UserType;
import com.fooluodi.broker.user.dao.UserInfoMapper;
import com.fooluodi.broker.user.po.UserInfo;
import com.fooluodi.broker.user.service.UserService;
import com.fooluodi.broker.util.CopyUtils;
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
        logger.info("get all users");

        List<UserInfo> allUsers = userInfoMapper.getAllUsers();

        logger.info("get sum:{}", allUsers == null ? 0 : allUsers.size());

        return CopyUtils.copyList(allUsers, UserInfoBo.class);
    }

    @Override
    public List<UserInfoBo> getAllAnonymousUser() {
        logger.info("getAllAnonymousUser");

        List<UserInfo> allUsers = userInfoMapper.getUserByType(UserType.ANONYMOUS_USER);

        logger.info("get sum:{}", allUsers == null ? 0 : allUsers.size());

        return CopyUtils.copyList(allUsers, UserInfoBo.class);
    }

    @Override
    public List<UserInfoBo> getAllValidUser() {
        logger.info("getAllValidUser");

        List<UserInfo> allUsers = userInfoMapper.getUserByType(UserType.NORMAL_USER);

        logger.info("get sum:{}", allUsers == null ? 0 : allUsers.size());

        return CopyUtils.copyList(allUsers, UserInfoBo.class);
    }
}