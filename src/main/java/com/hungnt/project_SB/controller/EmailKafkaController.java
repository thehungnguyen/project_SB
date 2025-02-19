package com.hungnt.project_SB.controller;

import com.hungnt.project_SB.dto.request.MailRequest;
import com.hungnt.project_SB.dto.response.ApiResponse;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailKafkaController {
    private final MailService mailService;

    // Mail Service
    @KafkaListener(topics = "sendEmail")
    public void sendEmail(User user) throws MessagingException {
        mailService.sendMailByKafka(user);
    }
}
