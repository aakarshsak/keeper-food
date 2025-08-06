package com.foodkeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp, String purpose) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Food Keeper - " + purpose + " OTP");
        message.setText(buildOtpEmailBody(otp, purpose));
        
        emailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to Food Keeper!");
        message.setText(buildWelcomeEmailBody(firstName));
        
        emailSender.send(message);
    }

    private String buildOtpEmailBody(String otp, String purpose) {
        return String.format(
            "Hello,\n\n" +
            "Your OTP for %s is: %s\n\n" +
            "This OTP will expire in 10 minutes.\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "Food Keeper Team",
            purpose, otp
        );
    }

    private String buildWelcomeEmailBody(String firstName) {
        return String.format(
            "Hello %s,\n\n" +
            "Welcome to Food Keeper! Your account has been successfully created.\n\n" +
            "You can now start tracking your food items and managing your diet.\n\n" +
            "Best regards,\n" +
            "Food Keeper Team",
            firstName
        );
    }
} 