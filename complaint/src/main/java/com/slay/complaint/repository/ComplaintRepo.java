package com.slay.complaint.repository;

import com.slay.complaint.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepo extends JpaRepository<ComplaintEntity, Integer> {
   List<ComplaintEntity> findAllBySenderId(int userId);
}
