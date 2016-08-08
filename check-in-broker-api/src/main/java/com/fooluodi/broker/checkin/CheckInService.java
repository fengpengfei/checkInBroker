package com.fooluodi.broker.checkin;

import com.fooluodi.broker.user.bo.UserInfoBo;

/**
 * Created by di on 7/8/2016.
 */
public interface CheckInService {
    /**
     *
     * @param userInfoBo
     * @return
     */
    boolean checkIn(UserInfoBo userInfoBo);
}
