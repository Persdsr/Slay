package com.slay.notification.service;

import com.slay.notification.dto.BirthdayNotificationDTO;
import com.slay.notification.dto.ForgotPasswordDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "birthday-notifications",
            groupId = "birthday-notification-group",
            containerFactory = "birthdayKafkaListenerContainerFactory"
    )
    public void consumeBirthdayNotification(BirthdayNotificationDTO birthdayNotification) {
        emailService.sendBirthdayEmail(birthdayNotification.getEmail(), birthdayNotification.getName());
    }

    @KafkaListener(topics = "forgot-password-notifications",
            groupId = "forgot-password-notification-group",
            containerFactory = "forgotPasswordKafkaListenerContainerFactory"
    )
    public void consumeForgotPasswordNotification(ForgotPasswordDTO forgotPasswordDTO) {
        emailService.sendForgotPasswordUrl(forgotPasswordDTO);
    }
}