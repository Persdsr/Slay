package com.slay.course.repository.user;

import com.slay.course.entity.user.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<MessageEntity, Long> {
}
