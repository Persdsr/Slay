package com.slay.course.service.user;

import com.slay.course.DTO.response.user.BirthdayNotificationDTO;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.repository.user.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BirthdayService {

    private final UserRepo userRepository;

    private final KafkaTemplate<String, BirthdayNotificationDTO> kafkaTemplate;

    public BirthdayService(UserRepo userRepository, KafkaTemplate<String, BirthdayNotificationDTO> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void checkBirthdays() {
        LocalDate today = LocalDate.now();
        List<UserEntity> allUsers = userRepository.findAll();

        for (UserEntity user : allUsers) {
            if (user.getBirthday() != null) {
                LocalDate birthday = user.getBirthday().toLocalDate();
                if (birthday.getDayOfMonth() == today.getDayOfMonth() &&
                        birthday.getMonth() == today.getMonth()) {
                    CompletableFuture<SendResult<String,BirthdayNotificationDTO>> future = kafkaTemplate.send("birthday-notifications", new BirthdayNotificationDTO(user.getEmail(), user.getName()));
                    future.whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send message: {}", exception.getMessage());
                        } else {
                            log.info("Message sent successfully {}", result.getRecordMetadata());
                        }
                    });
                }

            }
        }
    }
}