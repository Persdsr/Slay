package com.slay.support.repository;

import com.slay.support.entity.support.SupportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRequestRepo extends JpaRepository<SupportRequestEntity, Integer> {
   List<SupportRequestEntity> findAllBySender(Integer userId);
}
