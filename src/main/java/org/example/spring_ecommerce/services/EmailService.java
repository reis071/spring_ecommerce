package org.example.spring_ecommerce.services;


import org.example.spring_ecommerce.controllers.dto.EmailDto;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("uolEcommerce@compasso.com.br");
        message.setTo(emailDto.to());
        message.setSubject(emailDto.subject());
        message.setText(emailDto.body());
        mailSender.send(message);
    }
}
