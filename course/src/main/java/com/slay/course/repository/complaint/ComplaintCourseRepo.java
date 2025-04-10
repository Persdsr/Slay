package com.slay.course.repository.complaint;

import com.slay.course.entity.complaint.ComplaintCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintCourseRepo extends JpaRepository<ComplaintCourseEntity, Integer> {
}
