package com.fooluodi.broker.user.service;

import com.fooluodi.broker.user.bo.UserInfoBo;

import java.util.List;

/**
 * Created by di on 7/8/2016.
 */
public interface UserService {
    /**
     * 获取所有用户
     * @return
     */
    List<UserInfoBo> getAllUsers();

    /**
     * 获取所有匿名用户
     * @return
     */
    List<UserInfoBo> getAllAnonymousUser();

    /**
     * 获取所有非匿名用户
     * @return
     */
    List<UserInfoBo> getAllValidUser();

    /**
     * 添加新用户
     * @param userInfoBo
     * @return
     */
    int addUser(UserInfoBo userInfoBo);

    int addUser(String userName, String passwd, String session, String mailAddress);

    int addUser(String validSession, String mailAddress);

    /**
     *
     * @param userId
     * @return
     */
    UserInfoBo getUserById(int userId);

    /**
     * 过滤用户, 返回需要打卡的用户
     * @param users
     * @return
     */
    List<UserInfoBo> filterUers(List<UserInfoBo> users);
}
