package com.slay.course.service.permission;

import com.slay.course.entity.support.SupportRequestEntity;
import com.slay.course.exception.NotFoundException;
import com.slay.course.repository.support.SupportRequestRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SupportPermissionService {

    private final SupportRequestRepo supportRequestRepo;

    public boolean isSupportSender(int supportId, String username) {
        SupportRequestEntity supportRequestEntity = supportRequestRepo.findById(supportId)
                .orElseThrow(() -> NotFoundException.builder().build());

        return supportRequestEntity.getSender().getUsername().equals(username);
    }


}