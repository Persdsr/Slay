package com.slay.complaint.dto.response.complaint;

import com.slay.complaint.dto.response.course.TrainingCourseLiteDTO;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;
import com.slay.complaint.entity.ComplaintCourseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Класс dto для передачи данных о жалобе на курс ({@link ComplaintCourseEntity}).
 */
@Data
public class ComplaintCourseDTO implements Serializable {
    private int id;
    private String sender;
    private String description;
    private String localComplaintType;
    private String complaintType;
    private LocalDateTime createdAt;
    private String resolvedBy;
    private boolean isResolved;
    private TrainingCourseLiteDTO course;

    /**
     * Преобразует сущность жалобы на курс ({@link ComplaintCourseEntity}) в dto.
     *
     * @param complaintEntity сущность жалобы на курс, которую нужно преобразовать.
     * @return объект {@link ComplaintCourseDTO}, содержащий данные о жалобе на курс.
     */
    public static ComplaintCourseDTO toModel(ComplaintCourseEntity complaintEntity) {
        ComplaintCourseDTO complaintDto = new ComplaintCourseDTO();
        complaintDto.setId(complaintEntity.getId());
        complaintDto.setDescription(complaintEntity.getDescription());
        complaintDto.setCreatedAt(complaintEntity.getCreatedAt());
        complaintDto.setDescription(complaintEntity.getDescription());
        //complaintDto.setSender(complaintEntity.getSender().getUsername());
        complaintDto.setComplaintType(complaintEntity.getComplaintType().name());
        complaintDto.setResolved(complaintEntity.isResolved());
        /*if (complaintEntity.getResolvedBy() != null) {
            complaintDto.setResolvedBy(complaintEntity.getResolvedBy().getUsername());
        }*/

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        /* Преобразование типа жалобы на локальный язык */
        if (complaintEntity.getComplaintType() != null) {
            complaintDto.setLocalComplaintType(bundle.getString(complaintEntity.getComplaintType().name()));
        } else {
            complaintDto.setLocalComplaintType("UNKNOWN");
        }

        return complaintDto;
    }
}
