package com.fooluodi.broker.operation.log.service.impl;

import com.fooluodi.broker.exception.SystemException;
import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.dao.LogMapper;
import com.fooluodi.broker.operation.log.exception.LogExceptionCode;
import com.fooluodi.broker.operation.log.po.Log;
import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.operation.log.worker.OperationLogSaver;
import com.fooluodi.broker.util.CopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by di on 7/8/2016.
 */
@Service
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

    @Override
    public List<LogBo> getAllLogs() {
        logger.info("get all logs");

        List<Log> all;
        try{
            all = logMapper.getAll();
        }catch (Exception e){
            logger.error("get all log failed!", e);
            throw new SystemException(LogExceptionCode.GET_LOG_ERROR, e);
        }

        logger.info("get sum:{}", all);

        List<LogBo> bos= CopyUtils.copyList(all, LogBo.class);

        return bos;
    }
}
