package com.slay.course.DTO.response.course;

import com.slay.course.entity.course.TrainingCourseStepEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrainingCourseCroppedStepDTO implements Serializable {
    private String title;
    private String description;

    public static TrainingCourseCroppedStepDTO toModel(TrainingCourseStepEntity trainingCourseStep) {
        TrainingCourseCroppedStepDTO stepDTO = new TrainingCourseCroppedStepDTO();
        stepDTO.setTitle(trainingCourseStep.getTitle());
        stepDTO.setDescription(trainingCourseStep.getDescription());

        return stepDTO;
    }
}
