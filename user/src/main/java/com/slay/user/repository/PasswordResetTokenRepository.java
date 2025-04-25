package com.slay.user.repository;

import com.slay.user.entity.user.PasswordResetToken;
import com.slay.user.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(UserEntity user);
}