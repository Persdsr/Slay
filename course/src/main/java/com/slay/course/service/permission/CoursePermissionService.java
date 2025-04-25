package com.slay.course.service.permission;

import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.exception.TrainingCourseNotFoundException;
import com.slay.course.repository.course.TrainingCourseRepo;
import org.springframework.stereotype.Component;

@Component
public class CoursePermissionService {

    private final TrainingCourseRepo trainingCourseRepo;

    public CoursePermissionService(TrainingCourseRepo trainingCourseRepo) {
        this.trainingCourseRepo = trainingCourseRepo;
    }

    public boolean isCourseAuthorById(int courseId, Integer userId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId)
                .orElseThrow(() -> TrainingCourseNotFoundException.builder().build());

        return trainingCourse.getAuthorId() == userId;
    }

    public boolean isCourseBuyer(int courseId, Integer userId) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(courseId).orElseThrow(
                () -> TrainingCourseNotFoundException.builder().build()
        );

        return trainingCourse.getBuyers().stream()
                .anyMatch(courseBuyer -> courseBuyer.equals(userId));
    }
}