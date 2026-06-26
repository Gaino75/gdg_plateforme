// service/EmailService.java
package com.gdg.service_notifications.service;

import com.gdg.service_notifications.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String message, Notification.TypeNotification type) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Construction du contenu HTML
            Context context = new Context();
            context.setVariable("message", message);
            context.setVariable("type", type.name());
            context.setVariable("date", java.time.LocalDateTime.now().toString());

            String htmlContent = templateEngine.process("notification-email", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Email envoyé avec succès à {}", to);

        } catch (MessagingException e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            throw new RuntimeException("Erreur d'envoi d'email", e);
        }
    }
}