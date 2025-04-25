package com.slay.complaint.dto.response.complaint;

import com.slay.complaint.entity.ComplaintEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

@Data
public class ComplaintDTO implements Serializable {
    @Schema(description = "ID жалобы", example = "1")
    private int id;

    @Schema(description = "Username отправителя жалобы", example = "John123")
    private String sender;

    @Schema(description = "Описание жалобы", example = "Материал содержит..")
    private String description;

    @Schema(description = "Тип жалобы", example = "COURSE")
    private String complaintType;

    @Schema(description = "Дата и время создания жалобы", example = "2025-03-12T15:25:47.729469")
    private LocalDateTime createdAt;

    @Schema(description = "Username админа или модератора решивший жалобу", example = "Moderator123")
    private String resolvedBy;

    @Schema(description = "Статус запроса (открыт/закрыт)", example = "false")
    private boolean isResolved;

    public static ComplaintDTO toModel(ComplaintEntity complaintEntity) {
        ComplaintDTO complaintDto = new ComplaintDTO();
        complaintDto.setId(complaintEntity.getId());
        complaintDto.setDescription(complaintEntity.getDescription());
        complaintDto.setCreatedAt(complaintEntity.getCreatedAt());
        complaintDto.setDescription(complaintEntity.getDescription());
        //complaintDto.setSender(complaintEntity.getSender().getUsername());
        complaintDto.setResolved(complaintEntity.isResolved());
     /*   if (complaintEntity.getResolvedBy() != null) {
            complaintDto.setResolvedBy(complaintEntity.getResolvedBy().getUsername());
        }*/

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        if (complaintEntity.getComplaintType() != null) {
            complaintDto.setComplaintType(bundle.getString(complaintEntity.getComplaintType().name()));
        } else {
            complaintDto.setComplaintType("UNKNOWN");
        }

        return complaintDto;
    }
}
