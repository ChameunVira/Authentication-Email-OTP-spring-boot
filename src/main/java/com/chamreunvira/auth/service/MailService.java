package com.chamreunvira.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail , String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to Our Platform!");
        message.setText("Hello " + name + ",\n\nThank for registering with us!\n\nChamreun Vira Team.");
        mailSender.send(message);
    }

    public void sendResetOptEmail(String toEmail , String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for resetting your password is " + otp +
                ". Use this OTP to processed with resetting your password.");
        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail , String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Account verification OTP");
        message.setText("Your OTP is " + otp + ". Verify your account using this OTP.");
        mailSender.send(message);
    }

}
