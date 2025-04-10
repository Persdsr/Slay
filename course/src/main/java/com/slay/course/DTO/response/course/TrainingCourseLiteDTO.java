package com.slay.course.DTO.response.course;

import com.slay.course.DTO.response.user.UserLiteDTO;
import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
        trainingCourseLiteDTO.setAuthor(UserLiteDTO.toModel(trainingCourseEntity.getAuthor()));


        return trainingCourseLiteDTO;
    }


}
