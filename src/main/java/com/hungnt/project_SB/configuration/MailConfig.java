package com.hungnt.project_SB.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.email}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.isSSL}")
    private boolean isSSL;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        //Thong tin cau hinh
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(email);
        javaMailSender.setPassword(password);
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", isSSL);
        properties.put("mail.smtp.from", email);
        properties.put("mail.debug", "true");

        return javaMailSender;
    }
}
