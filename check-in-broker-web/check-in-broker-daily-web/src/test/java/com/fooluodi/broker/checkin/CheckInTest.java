package com.fooluodi.broker.checkin;

import com.fooluodi.broker.user.bo.UserInfoBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by di on 21/8/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/root-context.xml"})
public class CheckInTest {
    @Resource
    private CheckInService checkInService;

    @Test
    public void testCheckIn(){
        UserInfoBo userInfoBo = new UserInfoBo();
        userInfoBo.setValidSession("_ele_uid=c364dd5e-a2ce-4f24-a6e6-a45b8264ce1b; JSESSIONID=70792FE3DCEF57E1858EC40D89330E43; ai=1; appId=1; ck=9f24d739-7125-4802-a1ee-743a1e99d5b2; date=1471600488086; deviceType=1; dt=1; token=463812497903b2e912d5af07934cf06b; u=1139407; uid=1139407; _ga=GA1.2.1570269042.1470399477");

        boolean b = checkInService.checkIn(userInfoBo);
        System.out.println(b);
    }
}
