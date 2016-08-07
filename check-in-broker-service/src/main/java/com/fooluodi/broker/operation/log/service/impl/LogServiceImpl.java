package com.fooluodi.broker.operation.log.service.impl;

import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.dao.LogMapper;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.operation.log.worker.OperationLogSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * Created by di on 7/8/2016.
 */
public class LogServiceImpl implements LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Resource(name = "asyncOperationLogThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private LogMapper logMapper;

    @Override
    public void asyncAddLog(LogBo log) {
        try {
            threadPoolTaskExecutor.execute(
                    new OperationLogSaver(
                            logMapper, log
                    )
            );
        } catch (Exception e) {
            logger.error("async save log error!", e);
        }
    }
}
