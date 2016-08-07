package com.fooluodi.broker.user.exception;

import com.fooluodi.broker.exception.Desc;
import com.fooluodi.broker.exception.IExceptionCode;

/**
 * Created by di on 7/8/2016.
 */
public enum  UserExceptionCode implements IExceptionCode {
    @Desc(value = "新增用户失败", code = "INSERT_USER_ERROR")
    INSERT_USER_ERROR,
}
