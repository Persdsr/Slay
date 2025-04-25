package com.slay.support.dto.request.support;

import com.slay.support.dto.response.user.UserLiteDTO;
import com.slay.support.entity.support.SupportRequestMessageEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SupportMessageRequest {
    private Long id;
    private int supportRequestId;
    private String message;
    private int senderId;
    private LocalDateTime createAt;
    private Set<String> images;

    public static SupportMessageRequest toModel(SupportRequestMessageEntity supportRequestMessageEntity) {
        SupportMessageRequest supportMessageRequest = new SupportMessageRequest();
        supportMessageRequest.setId(supportRequestMessageEntity.getId());
        supportMessageRequest.setMessage(supportRequestMessageEntity.getMessage());
        supportMessageRequest.setSenderId(supportMessageRequest.getSenderId());
        supportMessageRequest.setImages(supportRequestMessageEntity.getImages());
        supportMessageRequest.setCreateAt(supportRequestMessageEntity.getCreatedAt());
        supportMessageRequest.setSupportRequestId(supportRequestMessageEntity.getSupportRequest().getId());
        return supportMessageRequest;
    }
}

