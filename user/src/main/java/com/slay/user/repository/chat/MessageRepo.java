package com.slay.user.repository.chat;

import com.slay.user.entity.chat.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<MessageEntity, Long> {
}
