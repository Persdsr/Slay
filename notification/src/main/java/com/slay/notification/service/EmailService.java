package com.slay.notification.service;

import com.slay.notification.dto.ForgotPasswordDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBirthdayEmail(String to, String name) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("noreply@slaygym.ru");
            helper.setTo(to);
            helper.setSubject("Happy Birthday!");
            helper.setText("Дорогой(ая)" + name + "\n" +
                    "\n" +
                    "Поздравляем вас с Днем Рождения! \uD83C\uDF89\n" +
                    "Этот день — отличный повод не только отпраздновать, но и поставить новые цели.\n" +
                    "\n" +
                    "Мы верим, что вы сможете достичь всего, что задумали. Пусть ваш путь к здоровью и силе будет наполнен радостью, а каждая тренировка приносит удовольствие и результаты.\n" +
                    "\n" +
                    "Спасибо, что вы с нами. Мы всегда готовы помочь вам стать лучше! \uD83D\uDCAA");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendForgotPasswordUrl(ForgotPasswordDTO forgotPasswordDTO) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("noreply@slaygym.ru");
            helper.setTo(forgotPasswordDTO.getEmail());
            helper.setSubject(forgotPasswordDTO.getSubject());
            helper.setText(forgotPasswordDTO.getHtmlContent(), true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }
}
