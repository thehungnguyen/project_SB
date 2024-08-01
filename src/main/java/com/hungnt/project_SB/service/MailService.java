package com.hungnt.project_SB.service;

import com.hungnt.project_SB.dto.request.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
    /*
    Nhan 2 tham so:
    1. Thong tin Email(mailRequest)
    2. Noi dung Email (templateEmail - templateHTML)
    */
    public void sendMail(MailRequest mailRequest, String templateEmail) throws MessagingException {
        // Doi tuong dai dien cho Email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // MimeMessageHelper giup tao va cau hinh, xay dung Email
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");

        // Doi tuong chua du lieu de Render Template
        Context context = new Context();
        // Thiet lap cac bien tu mailReq
        context.setVariables(mailRequest.getProperties());
        // Render noi dung HTML
        String html = springTemplateEngine.process(templateEmail, context);

        // Thiet lap thuoc tinh Email
        mimeMessageHelper.setTo(mailRequest.getTo());
        mimeMessageHelper.setSubject(mailRequest.getSubject());
        mimeMessageHelper.setText(html, true);

        // Gui Email
        javaMailSender.send(mimeMessage);
    }
}
