package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
public class MailService {
//    private final JavaMailSender mailSender;
//
//    @Async
//    public void send() {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setSubject("Warning");
//        message.setText("Do homework");
//        message.setTo("f8rangiz@gmail.com");
//        mailSender.send(message);
//    }
}