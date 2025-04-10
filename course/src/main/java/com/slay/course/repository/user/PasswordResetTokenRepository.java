package com.slay.course.repository.user;

import com.slay.course.entity.user.PasswordResetToken;
import com.slay.course.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(UserEntity user);
}