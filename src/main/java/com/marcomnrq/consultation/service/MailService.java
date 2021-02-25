package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mail-template", context);
    }

    public void sendMail(NotificationEmail notificationEmail) {
        // TODO: connect to a real email server
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            // E-Mail details
            messageHelper.setFrom("ederiveroman@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(build(notificationEmail.getBody()));
        };

        /*
        try {
            mailSender.send(message);
        } catch (MailException exception){
            throw new CustomException("Exception ocurred when sending email");
        }
        */
    }
}
