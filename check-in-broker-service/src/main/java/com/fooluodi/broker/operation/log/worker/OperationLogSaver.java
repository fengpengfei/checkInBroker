package com.fooluodi.broker.operation.log.worker;

import com.fooluodi.broker.operation.log.bo.LogBo;
import com.fooluodi.broker.operation.log.dao.LogMapper;
import com.fooluodi.broker.operation.log.po.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Created by di on 7/8/2016.
 */
public class OperationLogSaver implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(OperationLogSaver.class);

    private LogMapper logMapper;

    private LogBo log;

    public OperationLogSaver(LogMapper logMapper, LogBo log) {
        this.logMapper = logMapper;
        this.log = log;
    }

    public OperationLogSaver() {

    }

    @Override
    public void run() {
        logger.info("save log, log:{}", log);

        Log logPo = new Log();
        BeanUtils.copyProperties(this.log, logPo);

        try {
            logMapper.insert(logPo);
        } catch (Exception e) {
            logger.error("insert log failed!", e);
        }
    }
}
