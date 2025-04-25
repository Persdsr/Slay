package com.slay.course.dto.response.course;

import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TrainingCourseCroppedDTO implements CourseResponse{

    private String name;
    private String description;
    private String poster;
    private String trailer;
    private int price;
    private Date createAt;
    private UserLiteDTO author;
    private String category;
    private Long courseBuyers;
    private int reviewsCount;
    private Set<TagDTO> tags = new HashSet<>();
    private int videoCount;
    private TrainingCourseCroppedStepDTO trainingCourseCroppedStep;

    public static TrainingCourseCroppedDTO toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseCroppedDTO trainingCourseCroppedDTO = new TrainingCourseCroppedDTO();
        trainingCourseCroppedDTO.setName(trainingCourseEntity.getName());
        trainingCourseCroppedDTO.setDescription(trainingCourseEntity.getDescription());
        trainingCourseCroppedDTO.setPoster(trainingCourseEntity.getPoster());
        trainingCourseCroppedDTO.setTrailer(trainingCourseEntity.getTrailer());
        trainingCourseCroppedDTO.setPrice(trainingCourseEntity.getPrice());
        trainingCourseCroppedDTO.setCreateAt(trainingCourseEntity.getCreatedAt());
        trainingCourseCroppedDTO.setCategory(trainingCourseEntity.getCategory().getName());
        trainingCourseCroppedDTO.setReviewsCount(trainingCourseEntity.getReviews().size());
        //trainingCourseCroppedDTO.setCourseBuyers(trainingCourseEntity.getCourseBuyers().stream().count());

        trainingCourseCroppedDTO.setVideoCount(
                (int) trainingCourseEntity.getTrainingCourseSteps().stream()
                        .flatMap(step -> step.getTrainingCourseStepDetails().stream())
                        .filter(detail -> detail.getVideo() != null && !detail.getVideo().isEmpty())
                        .count()
        );

        if (trainingCourseEntity.getTrainingCourseSteps() != null && !trainingCourseEntity.getTrainingCourseSteps().isEmpty()) {
            List<TrainingCourseStepDTO> steps = new ArrayList<>();
            steps.add(TrainingCourseStepDTO.toModel(trainingCourseEntity.getTrainingCourseSteps().get(0)));
            trainingCourseCroppedDTO.setTrainingCourseCroppedStep(TrainingCourseCroppedStepDTO.toModel(trainingCourseEntity.getTrainingCourseSteps().get(0)));
        }

        trainingCourseCroppedDTO.setTags(trainingCourseEntity.getTags().stream()
                .map(TagDTO::toModel)
                .collect(Collectors.toSet())
        );

        return trainingCourseCroppedDTO;
    }


}
