package com.slay.course.DTO.response.course;

import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrainingCourseMinimaInfo implements Serializable {
    private int id;
    private String name;

    public static TrainingCourseMinimaInfo toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseMinimaInfo trainingCourseMinimaInfo = new TrainingCourseMinimaInfo();
        trainingCourseMinimaInfo.setId(trainingCourseEntity.getId());
        trainingCourseMinimaInfo.setName(trainingCourseEntity.getName());
        return trainingCourseMinimaInfo;
    }
}
