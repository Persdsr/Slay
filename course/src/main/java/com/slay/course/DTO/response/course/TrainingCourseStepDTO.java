package com.slay.course.DTO.response.course;

import com.slay.course.entity.course.TrainingCourseStepEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TrainingCourseStepDTO implements Serializable {
    private String title;
    private String description;
    private List<TrainingCourseStepDetailDTO> trainingCourseStepDetails;

    public static TrainingCourseStepDTO toModel(TrainingCourseStepEntity trainingCourseStep) {
        TrainingCourseStepDTO stepDTO = new TrainingCourseStepDTO();
        stepDTO.setTitle(trainingCourseStep.getTitle());
        stepDTO.setDescription(trainingCourseStep.getDescription());

        List<TrainingCourseStepDetailDTO> details = trainingCourseStep.getTrainingCourseStepDetails()
                .stream()
                .map(TrainingCourseStepDetailDTO::toModel)
                .collect(Collectors.toList());

        stepDTO.setTrainingCourseStepDetails(details);
        return stepDTO;
    }
}
