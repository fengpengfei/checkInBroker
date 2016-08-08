package com.fooluodi.broker.operation.log.exception;

import com.fooluodi.broker.exception.Desc;
import com.fooluodi.broker.exception.IExceptionCode;

/**
 * Created by di on 7/8/2016.
 */
public enum LogExceptionCode implements IExceptionCode {
    @Desc(value = "获取log失败", code = "GET_LOG_ERROR")
    GET_LOG_ERROR,
}
