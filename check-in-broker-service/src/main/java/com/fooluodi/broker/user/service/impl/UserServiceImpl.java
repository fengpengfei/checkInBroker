package com.fooluodi.broker.user.service.impl;

import com.fooluodi.broker.exception.SystemException;
import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.constant.LogType;
import com.fooluodi.broker.operation.log.po.Log;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import com.fooluodi.broker.user.constant.UserDefaultConstant;
import com.fooluodi.broker.user.constant.UserType;
import com.fooluodi.broker.user.dao.UserInfoMapper;
import com.fooluodi.broker.user.exception.UserExceptionCode;
import com.fooluodi.broker.user.po.UserInfo;
import com.fooluodi.broker.user.service.UserService;
import com.fooluodi.broker.util.CopyUtils;
import com.fooluodi.broker.util.validate.function.ValidateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by di on 7/8/2016.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private LogService logService;

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

    @Override
    public int addUser(UserInfoBo userInfoBo) {
        logger.info("add new user:{}", userInfoBo);

        UserInfo userInfo = new UserInfo();

        BeanUtils.copyProperties(userInfoBo, userInfo);

        try {
            userInfoMapper.insert(userInfo);
        } catch (Exception e) {
            logger.error("insert error!", e);
            throw new SystemException(UserExceptionCode.INSERT_USER_ERROR);
        }

        this.saveLog(userInfoBo.getIsValid() == UserType.NORMAL_USER ? LogType.ADD_NEW_USER : LogType.ADD_NEW_ANONYMOUS_USER,
                userInfo.getId(), userInfo.toString());

        return userInfo.getId();
    }

    @Override
    public int addUser(String userName, String passwd, String session) {
        logger.info("add user, userName:{}, passwd:{}, session:{}", userName, passwd, session);

        UserInfoBo userInfo = new UserInfoBo();
        userInfo.setUserName(userName);
        userInfo.setUserPasswd(passwd);
        userInfo.setIsValid(UserType.NORMAL_USER);
        userInfo.setValidSession(session);
        userInfo.setCheckTimes(0);
        userInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        ValidateHelper.validate(userInfo);

        return this.addUser(userInfo);
    }

    @Override
    public int addUser(String validSession) {
        logger.info("add anonymous user, session:{}", validSession);

        UserInfoBo userInfo = new UserInfoBo();
        userInfo.setUserName(UserDefaultConstant.ANONYMOUS_USER_NAME + UUID.randomUUID().toString());
        userInfo.setUserPasswd(UserDefaultConstant.DEFAULT_PASSWD);
        userInfo.setIsValid(UserType.ANONYMOUS_USER);
        userInfo.setValidSession(UserDefaultConstant.DEFAULT_SESSION);
        userInfo.setCheckTimes(0);
        userInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        ValidateHelper.validate(userInfo);

        return this.addUser(userInfo);
    }

    private void saveLog(int type, int userId, String detail) {
        LogBo log = new LogBo();

        log.setOpType(type);
        log.setUserId(userId);
        log.setDetail(detail);
        log.setCreateTime(new Timestamp(System.currentTimeMillis()));
        log.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        logService.asyncAddLog(log);
    }
}
