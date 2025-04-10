package com.slay.course.repository.complaint;

import com.slay.course.entity.complaint.ComplaintUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ComplaintUserRepo extends JpaRepository<ComplaintUserEntity, Integer> {
    Set<ComplaintUserEntity> findAllByBannedTrue();
}
