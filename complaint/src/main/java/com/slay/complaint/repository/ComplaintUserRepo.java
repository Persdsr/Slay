package com.slay.complaint.repository;

import com.slay.complaint.entity.ComplaintUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ComplaintUserRepo extends JpaRepository<ComplaintUserEntity, Integer> {
    Set<ComplaintUserEntity> findAllByBannedTrue();
}
