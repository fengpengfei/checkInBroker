package com.fooluodi.broker.mail.service.impl;

import com.fooluodi.broker.mail.service.MailService;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by di on 1/9/2016.
 */
@Service
public class MailServiceImpl implements MailService {

    private static final String SENDCLOUD_SMTP_HOST = "smtp.sendcloud.net";
    private static final int SENDCLOUD_SMTP_PORT = 25;

    @Value("")
    private String apiUser;
    @Value("")
    private String apiKey;

    @Override
    public void sendMail(String address, String context, String subject) throws UnsupportedEncodingException, MessagingException {

        // 配置javamail
        Properties props = System.getProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SENDCLOUD_SMTP_HOST);
        props.put("mail.smtp.port", SENDCLOUD_SMTP_PORT);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.connectiontimeout", 180);
        props.put("mail.smtp.timeout", 600);
        props.setProperty("mail.mime.encodefilename", "true");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(apiUser, apiKey);
            }
        });

        String to = address;

        SMTPTransport transport = (SMTPTransport) mailSession.getTransport("smtp");

        MimeMessage message = new MimeMessage(mailSession);
        // 发信人
        message.setFrom(new InternetAddress("from@sendcloud.org", "fromname", "UTF-8"));
        // 收件人地址
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // 邮件主题
        message.setSubject("SendCloud java smtp example", "UTF-8");

        Multipart multipart = new MimeMultipart("alternative");

        // 添加html形式的邮件正文
        String html = "<html><head></head><body>" + "<p>欢迎使用<a href='http://sendcloud.sohu.com'>SendCloud!</a></p>" + "</body></html> ";
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
        contentPart.setHeader("Content-Transfer-Encoding", "base64");
        contentPart.setContent(html, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);

        // 添加附件 ( smtp 方式没法使用文件流 )
        File file = new File("/path/file");
        BodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
        multipart.addBodyPart(attachmentBodyPart);
        message.setContent(multipart);

        // 连接sendcloud服务器，发送邮件
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

        String messageId = getMessage(transport.getLastServerResponse());
        String emailId = messageId + "0$" + to;
        System.out.println("messageId:" + messageId);
        System.out.println("emailId:" + emailId);
        transport.close();
    }

    private static String getMessage(String reply) {
        String[] arr = reply.split("#");

        String messageId = null;

        if (arr[0].equalsIgnoreCase("250 ")) {
            messageId = arr[1];
        }

        return messageId;
    }

    @Override
    public void sendMail(List<String> address, String context, String subject) {

    }
}
