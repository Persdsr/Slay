package com.slay.course.service.permission;

import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.user.UserRepo;
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