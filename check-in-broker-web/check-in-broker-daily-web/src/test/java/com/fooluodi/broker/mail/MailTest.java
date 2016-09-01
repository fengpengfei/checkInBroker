package com.fooluodi.broker.mail;

import com.fooluodi.broker.AbstractTestCase;
import com.fooluodi.broker.mail.service.MailService;
import org.junit.Test;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Created by di on 1/9/2016.
 */
public class MailTest extends AbstractTestCase {
    @Resource
    private MailService mailService;

    @Test
    public void testMail(){
        String address = "LuoDi_Nate@163.com";
        try {
            mailService.sendMail(address, "hi", "test");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
