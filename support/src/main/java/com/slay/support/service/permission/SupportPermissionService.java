package com.slay.support.service.permission;

import com.slay.support.entity.support.SupportRequestEntity;
import com.slay.support.exception.NotFoundException;
import com.slay.support.repository.SupportRequestRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SupportPermissionService {

    private final SupportRequestRepo supportRequestRepo;

    public boolean isSupportSender(int supportId, Integer userId) {
        SupportRequestEntity supportRequestEntity = supportRequestRepo.findById(supportId)
                .orElseThrow(() -> NotFoundException.builder().build());

        return userId.equals(supportRequestEntity.getSender());
    }


}