package com.slay.course.repository.course;

import com.slay.course.entity.course.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCourseReviewRepo extends JpaRepository<ReviewEntity, Integer> {
}
