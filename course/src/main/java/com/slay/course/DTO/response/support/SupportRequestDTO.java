package com.slay.course.DTO.response.support;

import com.slay.course.DTO.request.support.SupportMessageRequest;
import com.slay.course.entity.support.SupportRequestEntity;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Data
public class SupportRequestDTO {
    private int id;
    private String senderUsername;
    private String receiverUsername;
    private String email;
    private String subject;
    private List<SupportMessageRequest> messages;
    private String requestType;
    private LocalDateTime createAt;
    private LocalDateTime resolvedAt;
    private boolean resolved;

    public static SupportRequestDTO toModel(SupportRequestEntity supportRequestEntity) {
        SupportRequestDTO supportRequestDTO = new SupportRequestDTO();
        supportRequestDTO.setId(supportRequestEntity.getId());
        supportRequestDTO.setEmail(supportRequestEntity.getEmail());
        supportRequestDTO.setSubject(supportRequestEntity.getSubject());
        List<SupportMessageRequest> messages = supportRequestEntity.getMessages().stream().map(SupportMessageRequest::toModel).collect(Collectors.toList());
        supportRequestDTO.setMessages(messages);
        supportRequestDTO.setCreateAt(supportRequestEntity.getCreatedAt());
        supportRequestDTO.setResolvedAt(supportRequestEntity.getResolvedAt());
        supportRequestDTO.setResolved(supportRequestEntity.isResolved());

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        if (supportRequestEntity.getRequestType() != null) {
            supportRequestDTO.setRequestType(bundle.getString(supportRequestEntity.getRequestType().name()));
        } else {
            supportRequestDTO.setRequestType("UNKNOWN");
        }


        if (supportRequestEntity.getResolvedBy() != null) {
            supportRequestDTO.setReceiverUsername(supportRequestEntity.getResolvedBy().getUsername());
        }

        supportRequestDTO.setSenderUsername(supportRequestEntity.getSender().getUsername());

        return supportRequestDTO;

    }

}
