package com.fooluodi.broker.mail.service;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by di on 1/9/2016.
 */
public interface MailService {
    /**
     * 给单个用户发邮件
     * @param address
     * @param context
     * @param subject
     */
    void sendMail(String address, String context, String subject) throws UnsupportedEncodingException, MessagingException;

    /**
     * 给一群用户发邮件
     * @param address
     * @param context
     * @param subject
     */
    void sendMail(List<String> address, String context, String subject);
}
