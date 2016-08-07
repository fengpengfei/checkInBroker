package com.fooluodi.broker.operation.log.bo;

import java.sql.Timestamp;

/**
 * Created by di on 7/8/2016.
 */
public class LogBo {

    private Integer id;

    private Integer opType;

    private Integer userId;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String detail;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogBo{");
        sb.append("id=").append(id);
        sb.append(", opType=").append(opType);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", detail='").append(detail).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}