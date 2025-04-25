package com.slay.user.service.kafka;

import com.slay.user.dto.request.course.CourseCreateEvent;
import com.slay.user.dto.response.course.CoursePurchasedEvent;
import com.slay.user.entity.user.UserEntity;
import com.slay.user.exception.UserNotFoundException;
import com.slay.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CourseConsumer {
    private final UserRepo userRepo;

    public CourseConsumer(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    @KafkaListener(topics = "buy-course",
            groupId = "buy-course-group",
            containerFactory = "buyCourseKafkaListenerContainerFactory"
    )
    public void consumeBuyCourse(CoursePurchasedEvent coursePurchasedEvent) {
        UserEntity user = userRepo.findById(coursePurchasedEvent.getUserId()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        user.getPurchasedCourseIds().add(coursePurchasedEvent.getCourseId());
        userRepo.save(user);
    }

    @Transactional
    @KafkaListener(topics = "create-course",
            groupId = "create-course-group",
            containerFactory = "createCourseKafkaListenerContainerFactory"
    )
    public void consumeCreateCourse(CourseCreateEvent courseCreateEvent) {
        UserEntity user = userRepo.findById(courseCreateEvent.getAuthorId()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        user.getCoursesIds().add(courseCreateEvent.getCourseId());
        userRepo.save(user);
    }
}
