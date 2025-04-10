package com.slay.course.repository.course;

import com.slay.course.entity.course.TrainingCourseStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingCourseStepRepo extends JpaRepository<TrainingCourseStepEntity, Long> {

}
