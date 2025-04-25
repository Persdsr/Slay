package com.slay.course.dto.response.course;


import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
public class TrainingCourseLiteDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String poster;
    private int price;
    private Date createAt;
    private UserLiteDTO author;
    private String category;
    private Long courseBuyers;
    private Long reviewsCount;

    public static TrainingCourseLiteDTO toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseLiteDTO trainingCourseLiteDTO = new TrainingCourseLiteDTO();
        trainingCourseLiteDTO.setName(trainingCourseEntity.getName());
        trainingCourseLiteDTO.setId(trainingCourseEntity.getId());
        trainingCourseLiteDTO.setDescription(trainingCourseEntity.getDescription());

        if (trainingCourseEntity.getCategory() != null) {
            trainingCourseLiteDTO.setCategory(trainingCourseEntity.getCategory().getName());
        }

        trainingCourseLiteDTO.setPoster(trainingCourseEntity.getPoster());
        trainingCourseLiteDTO.setPrice(trainingCourseEntity.getPrice());
        trainingCourseLiteDTO.setCreateAt(trainingCourseEntity.getCreatedAt());

        return trainingCourseLiteDTO;
    }

    public static TrainingCourseLiteDTO toModel(TrainingCourseEntity entity, Map<Integer, UserLiteDTO> authorMap) {
        TrainingCourseLiteDTO dto = new TrainingCourseLiteDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPoster(entity.getPoster());
        dto.setPrice(entity.getPrice());
        dto.setCreateAt(entity.getCreatedAt());

        if (entity.getCategory() != null) {
            dto.setCategory(entity.getCategory().getName());
        }

        dto.setAuthor(authorMap.get(entity.getAuthorId()));
        dto.setCourseBuyers((long) entity.getBuyers().size());
        dto.setReviewsCount((long) entity.getReviews().size());

        return dto;
    }
}
