package com.parkingtime.services;

import com.parkingtime.models.UserResetPasswordToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public String generateResetPasswordContent(UserResetPasswordToken userResetPasswordToken) {
        Context context = new Context();
        context.setVariable("email", userResetPasswordToken.getUser().getEmail());
        context.setVariable("token", userResetPasswordToken.getToken());
        return templateEngine.process("emails/reset-password", context);
    }

    public void sendMail(String receiver, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setTo(receiver);
        javaMailSender.send(mimeMessage);
    }
}
