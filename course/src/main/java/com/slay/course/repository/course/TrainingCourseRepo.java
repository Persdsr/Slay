package com.slay.course.repository.course;

import com.slay.course.entity.course.TrainingCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingCourseRepo extends JpaRepository<TrainingCourseEntity, Integer> {
    Optional<TrainingCourseEntity> findByName(String courseName);
    List<TrainingCourseEntity> findByNameContainingIgnoreCase(String name);
    List<TrainingCourseEntity> findAllByAuthorId(Integer authorId);
}
