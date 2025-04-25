package com.slay.complaint.repository;

import com.slay.complaint.entity.ComplaintCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintCourseRepo extends JpaRepository<ComplaintCourseEntity, Integer> {
}
