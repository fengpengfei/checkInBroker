package com.fooluodi.broker.checkin;

import com.fooluodi.broker.operation.log.service.LogService;
import com.fooluodi.broker.user.bo.UserInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by di on 7/8/2016.
 */
public class CheckInServiceImpl implements  CheckInService {
    private static final Logger logger = LoggerFactory.getLogger(CheckInServiceImpl.class);

    @Resource
    private LogService logService;

    @Override
    public boolean checkIn(UserInfoBo userInfoBo) {

        return false;
    }
}
