package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.NotificationEmail;
import com.marcomnrq.consultation.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    public String buildMail(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("email-template", context);
    }

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        // TODO: connect to a real email server
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            // E-Mail details
            messageHelper.setFrom("no-reply@testingmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(buildMail(notificationEmail.getBody()));
        };

        try {
            mailSender.send(message);
        } catch (MailException exception){
            System.out.println(exception);
            throw new CustomException("Exception occurred when sending email", exception);
        }

    }
}
