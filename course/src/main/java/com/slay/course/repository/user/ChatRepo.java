package com.slay.course.repository.user;

import com.slay.course.entity.user.ChatEntity;
import com.slay.course.entity.user.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepo extends JpaRepository<ChatEntity, Integer> {
    List<ChatEntity> findByMembers(UserEntity member);

    @Query("SELECT c FROM chat c WHERE :user1 MEMBER OF c.members AND :user2 MEMBER OF c.members")
    Optional<ChatEntity> findChatByMembers(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);
}
