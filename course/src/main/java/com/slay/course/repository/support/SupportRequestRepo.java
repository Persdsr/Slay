package com.slay.course.repository.support;

import com.slay.course.entity.support.SupportRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRequestRepo extends JpaRepository<SupportRequestEntity, Integer> {
    List<SupportRequestEntity> findAllBySenderUsername(String username);
}
