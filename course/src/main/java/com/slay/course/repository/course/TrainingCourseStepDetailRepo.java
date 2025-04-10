package com.slay.course.repository.course;

import com.slay.course.entity.course.TrainingCourseStepDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingCourseStepDetailRepo extends JpaRepository<TrainingCourseStepDetailEntity, Long> {

}
