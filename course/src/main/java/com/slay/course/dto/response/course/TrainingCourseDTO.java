package com.slay.course.dto.response.course;

import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TrainingCourseDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String poster;
    private String trailer;
    private int price;
    private Date createAt;
    //private UserLiteDTO author;
    private String category;
    private Long courseBuyers;
    private Long reviewsCount;
    private List<TrainingCourseStepDTO> trainingCourseSteps;

    public static TrainingCourseDTO toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseDTO trainingCourseDTO = new TrainingCourseDTO();

        trainingCourseDTO.setName(trainingCourseEntity.getName());
        trainingCourseDTO.setDescription(trainingCourseEntity.getDescription());
        trainingCourseDTO.setId(trainingCourseEntity.getId());

        trainingCourseDTO.setPoster(trainingCourseEntity.getPoster());
        trainingCourseDTO.setTrailer(trainingCourseDTO.getTrailer());

        trainingCourseDTO.setPrice(trainingCourseEntity.getPrice());
        trainingCourseDTO.setCreateAt(trainingCourseEntity.getCreatedAt());

        //trainingCourseDTO.setAuthor(UserLiteDTO.toModel(trainingCourseEntity.getAuthor()));
        trainingCourseDTO.setCategory(trainingCourseEntity.getCategory().getName());

        //trainingCourseDTO.setCourseBuyers((long) trainingCourseEntity.getCourseBuyers().size());
        trainingCourseDTO.setReviewsCount((long) trainingCourseEntity.getReviews().size());

        List<TrainingCourseStepDTO> stepDTOs = trainingCourseEntity.getTrainingCourseSteps()
                .stream()
                .map(TrainingCourseStepDTO::toModel)
                .collect(Collectors.toList());
        trainingCourseDTO.setTrainingCourseSteps(stepDTOs);

        return trainingCourseDTO;
    }
}