package com.slay.course.repository.support;

import com.slay.course.entity.support.SupportRequestMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRequestMessageRepo extends JpaRepository<SupportRequestMessageEntity, Long> {
}
