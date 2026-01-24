package com.posapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.api.baseUrl}")
    private String baseUrl;

    @Value("${spring.mail.username:noemail}")
    private String fromEmail;

    public void sendResetPasswordEmail(String to, String resetToken) {
        if (mailSender == null) {
            logger.warn("Email service not configured. Password reset link: {}/auth/reset-password?token={}",
                    baseUrl, resetToken);
            return;
        }

        try {
            String resetLink = baseUrl + "/auth/reset-password?token=" + resetToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("POS System - Password Reset");
            message.setText("You requested a password reset for your POS account.\n\n" +
                    "Click here to reset your password: " + resetLink + "\n\n"+
                    "If you didn't request this, please ignore this email.");

            mailSender.send(message);
            logger.info("Password reset email sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", to, e);
        }
    }
}
