package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.MailRequest;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.service.MailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class EmailKafkaController {
    @Autowired
    private MailService mailService;

    // Mail Service
    @KafkaListener(topics = "sendEmail")
    public void sendEmail(User user) throws MessagingException {
        log.info("Email will be sent to: " + user.getEmail() + ", please check your email.");
        MailRequest mailRequest = new MailRequest();

        mailRequest.setTo(user.getEmail());
        mailRequest.setSubject("XAC NHAN THONG TIN NGUOI DUNG");

        Map<String, Object> properties = new HashMap<>();

        properties.put("firstName", user.getFirstName());
        properties.put("lastName", user.getLastName());
        properties.put("username", user.getUsername());

        mailRequest.setProperties(properties);

        mailService.sendMail(mailRequest, "templateEmail");
    }
}
