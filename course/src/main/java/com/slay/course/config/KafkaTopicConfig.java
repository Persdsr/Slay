package com.slay.course.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic passwordResetTopic() {
        return TopicBuilder.name("forgot-password-notifications")
                .partitions(3)
                .replicas(2)
                .compact()
                .build();
    }

    @Bean
    public NewTopic birthdayTopic() {
        return TopicBuilder.name("birthday-notifications")
                .partitions(3)
                .replicas(0)
                .build();
    }

    @Bean
    public NewTopic buyCourseTopic() {
        return TopicBuilder.name("buy-course")
                .partitions(3)
                .replicas(0)
                .build();
    }

    @Bean
    public NewTopic createCourseTopic() {
        return TopicBuilder.name("create-course")
                .partitions(3)
                .replicas(1)
                .build();
    }
}