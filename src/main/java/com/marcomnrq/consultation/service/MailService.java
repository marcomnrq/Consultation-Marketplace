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

    public String buildMail(NotificationEmail notificationEmail) {
        Context context = new Context();
        context.setVariable("user", "¡Hola " + notificationEmail.getFullName() + "!");
        context.setVariable("message", notificationEmail.getBody());
        context.setVariable("buttonLink", notificationEmail.getButtonLink());
        context.setVariable("buttonText", notificationEmail.getButtonText());
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
            messageHelper.setText(buildMail(notificationEmail));
        };

        try {
            mailSender.send(message);
        } catch (MailException exception){
            throw new CustomException("Exception occurred when sending email", exception);
        }

    }
}
