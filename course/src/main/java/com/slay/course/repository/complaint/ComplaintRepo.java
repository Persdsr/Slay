package com.slay.course.repository.complaint;

import com.slay.course.entity.complaint.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepo extends JpaRepository<ComplaintEntity, Integer> {
    List<ComplaintEntity> findAllBySenderUsername(String username);
}
