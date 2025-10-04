package com.slay.course.dto.response.course;

import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.entity.course.TrainingCourseStepEntity;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TrainingCourseDetailDTO implements CourseResponse{
    private String name;
    private String description;
    private String poster;
    private String trailer;
    private int price;
    private Date createAt;
    private UserLiteDTO author;
    private String category;
    private Set<TagDTO> tags = new HashSet<>();
    private Long courseBuyers;
    private List<TrainingCourseStepDTO> trainingCourseSteps;
    //private boolean isChatting;
    private int reviewsCount;
    private int videoCount;

    public static TrainingCourseDetailDTO toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseDetailDTO trainingCourseDetailDTO = new TrainingCourseDetailDTO();
        trainingCourseDetailDTO.setName(trainingCourseEntity.getName());
        trainingCourseDetailDTO.setDescription(trainingCourseEntity.getDescription());
        trainingCourseDetailDTO.setPoster(trainingCourseEntity.getPoster());
        trainingCourseDetailDTO.setTrailer(trainingCourseEntity.getTrailer());
        trainingCourseDetailDTO.setPrice(trainingCourseEntity.getPrice());
        trainingCourseDetailDTO.setCreateAt(trainingCourseEntity.getCreatedAt());
        //trainingCourseDetailDTO.setAuthor(UserLiteDTO.toModel(trainingCourseEntity.getAuthor()));

        trainingCourseDetailDTO.setCategory(trainingCourseEntity.getCategory().getName());
        trainingCourseDetailDTO.setTags(trainingCourseEntity.getTags().stream().map(TagDTO::toModel).collect(Collectors.toSet()));


        trainingCourseDetailDTO.setReviewsCount(trainingCourseEntity.getReviews().size());
        //trainingCourseDetailDTO.setCourseBuyers(trainingCourseEntity.getCourseBuyers().stream().count());

        List<TrainingCourseStepDTO> steps = new ArrayList<>();

        for (TrainingCourseStepEntity trainingCourseSteps : trainingCourseEntity.getTrainingCourseSteps()) {
            steps.add(TrainingCourseStepDTO.toModel(trainingCourseSteps));
        }

        trainingCourseDetailDTO.setVideoCount(
                (int) trainingCourseEntity.getTrainingCourseSteps().stream()
                .flatMap(step -> step.getTrainingCourseStepDetails().stream())
                .filter(detail -> detail.getVideo() != null && !detail.getVideo().isEmpty())
                .count()
        );

        trainingCourseDetailDTO.setTrainingCourseSteps(steps);

        return trainingCourseDetailDTO;

    }

}
