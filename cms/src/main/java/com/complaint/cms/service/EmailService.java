package com.complaint.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset OTP – Sir C R Reddy College CMS");
            message.setText(
                "Dear Student,\n\n" +
                "Your OTP for password reset is:\n\n" +
                otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n" +
                "Do NOT share this OTP with anyone.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\n" +
                "Sir C R Reddy College for Women\n" +
                "Complaint Management System"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordChangedEmail(String toEmail, String studentName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Changed Successfully – Sir C R Reddy College CMS");
            message.setText(
                "Dear " + studentName + ",\n\n" +
                "Your password has been changed successfully.\n\n" +
                "If you did NOT make this change, please contact the college administration immediately.\n\n" +
                "Regards,\n" +
                "Sir C R Reddy College for Women\n" +
                "Complaint Management System"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }
}
