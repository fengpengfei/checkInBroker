package com.fooluodi.broker.user.po;

import java.sql.Timestamp;

public class UserInfo {
    private Integer id;

    private String userName;

    private String userPasswd;

    private String validSession;

    private Integer checkTimes;

    private Integer isValid;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserInfo{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPasswd='").append(userPasswd).append('\'');
        sb.append(", validSession='").append(validSession).append('\'');
        sb.append(", checkTimes=").append(checkTimes);
        sb.append(", isValid=").append(isValid);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getValidSession() {
        return validSession;
    }

    public void setValidSession(String validSession) {
        this.validSession = validSession;
    }

    public Integer getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}