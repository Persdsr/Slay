package com.slay.course.dto.response.course;

import com.slay.course.entity.course.TrainingCourseStepDetailEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrainingCourseStepDetailDTO implements Serializable {
    private String description;
    private String title;
    private String images;
    private String videos;

    public static TrainingCourseStepDetailDTO toModel(TrainingCourseStepDetailEntity detailEntity) {
        TrainingCourseStepDetailDTO detailDTO = new TrainingCourseStepDetailDTO();
        detailDTO.setDescription(detailEntity.getDescription());
        detailDTO.setTitle(detailEntity.getTitle());
        detailDTO.setVideos(detailEntity.getVideo());

        return detailDTO;
    }
}