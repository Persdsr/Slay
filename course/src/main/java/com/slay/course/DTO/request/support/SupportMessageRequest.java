package com.slay.course.DTO.request.support;

import com.slay.course.DTO.response.user.UserLiteDTO;
import com.slay.course.entity.support.SupportRequestMessageEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SupportMessageRequest {
    private Long id;
    private int supportRequestId;
    private String message;
    private UserLiteDTO sender;
    private LocalDateTime createAt;
    private Set<String> images;

    public static SupportMessageRequest toModel(SupportRequestMessageEntity supportRequestMessageEntity) {
        SupportMessageRequest supportMessageRequest = new SupportMessageRequest();
        supportMessageRequest.setId(supportRequestMessageEntity.getId());
        supportMessageRequest.setMessage(supportRequestMessageEntity.getMessage());
        supportMessageRequest.setSender(UserLiteDTO.toModel(supportRequestMessageEntity.getSender()));
        supportMessageRequest.setImages(supportRequestMessageEntity.getImages());
        supportMessageRequest.setCreateAt(supportRequestMessageEntity.getCreatedAt());
        supportMessageRequest.setSupportRequestId(supportRequestMessageEntity.getSupportRequest().getId());
        return supportMessageRequest;
    }
}

