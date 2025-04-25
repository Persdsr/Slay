package com.slay.user.service.permission;

import com.slay.user.entity.user.UserEntity;
import com.slay.user.exception.UserNotFoundException;
import com.slay.user.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserPermissionService {

    private final UserRepo userRepo;

    public boolean isCurrentUser(String username) {

        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.builder().build());

        return true;
    }


}