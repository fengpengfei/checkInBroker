package com.fooluodi.broker.operation.log.service;

import com.fooluodi.broker.operation.log.bo.LogBo;

import java.util.List;

/**
 * Created by di on 7/8/2016.
 */
public interface LogService {
    /**
     * 异步添加日志
     * @param log
     */
    void asyncAddLog(LogBo log);

    /**
     * 获取所有日志
     * @return
     */
    List<LogBo> getAllLogs();
}
