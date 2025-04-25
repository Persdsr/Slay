package com.slay.support.repository;

import com.slay.support.entity.support.SupportRequestMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRequestMessageRepo extends JpaRepository<SupportRequestMessageEntity, Long> {
}
