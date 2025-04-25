package com.slay.complaint.dto.response.complaint;

import com.slay.complaint.entity.ComplaintUserEntity;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

@Data
public class ComplaintUserDTO {
    private int id;
    private String sender;
    private String description;
    private String localComplaintType;
    private String complaintType;
    private LocalDateTime createdAt;
    private String resolvedBy;
    private boolean isResolved;
    private boolean banned;
    //private UserDTO reportedUser;

    public static ComplaintUserDTO toModel(ComplaintUserEntity complaintEntity) {
        ComplaintUserDTO complaintDto = new ComplaintUserDTO();
        complaintDto.setId(complaintEntity.getId());
        complaintDto.setDescription(complaintEntity.getDescription());
        complaintDto.setCreatedAt(complaintEntity.getCreatedAt());
        complaintDto.setDescription(complaintEntity.getDescription());
        //complaintDto.setSender(complaintEntity.getSender().getUsername());
        complaintDto.setResolved(complaintEntity.isResolved());
        /*if (complaintEntity.getResolvedBy() != null) {
            complaintDto.setResolvedBy(complaintEntity.getResolvedBy().getUsername());
        }*/
        complaintDto.setComplaintType(complaintEntity.getComplaintType().name());
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        if (complaintEntity.getComplaintType() != null) {
            complaintDto.setLocalComplaintType(bundle.getString(complaintEntity.getComplaintType().name()));
        } else {
            complaintDto.setLocalComplaintType("UNKNOWN");
        }

        //complaintDto.setReportedUser(UserDTO.toModel(complaintEntity.getReportedUser()));
        complaintDto.setBanned(complaintEntity.isBanned());

        return complaintDto;
    }
}
