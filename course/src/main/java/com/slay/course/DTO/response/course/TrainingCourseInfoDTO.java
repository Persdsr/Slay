package com.slay.course.DTO.response.course;

import com.slay.course.DTO.response.review.ReviewDTO;
import com.slay.course.DTO.response.user.UserLiteDTO;
import com.slay.course.entity.course.ReviewEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TrainingCourseInfoDTO implements Serializable {
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
    private Set<ReviewDTO> reviews;

    public static TrainingCourseInfoDTO toModel(TrainingCourseEntity trainingCourseEntity) {
        TrainingCourseInfoDTO trainingCourseInfoDTO = new TrainingCourseInfoDTO();
        trainingCourseInfoDTO.setName(trainingCourseEntity.getName());
        trainingCourseInfoDTO.setId(trainingCourseEntity.getId());
        trainingCourseInfoDTO.setDescription(trainingCourseEntity.getDescription());

        if (trainingCourseEntity.getCategory() != null) {
            trainingCourseInfoDTO.setCategory(trainingCourseEntity.getCategory().getName());
        }

        trainingCourseInfoDTO.setPoster(trainingCourseEntity.getPoster());
        trainingCourseInfoDTO.setPrice(trainingCourseEntity.getPrice());
        trainingCourseInfoDTO.setCreateAt(trainingCourseEntity.getCreatedAt());
        trainingCourseInfoDTO.setAuthor(UserLiteDTO.toModel(trainingCourseEntity.getAuthor()));

        trainingCourseInfoDTO.setReviews(trainingCourseEntity.getReviews().stream()
                .sorted(Comparator.comparing(ReviewEntity::getCreatedAt).reversed())
                .map(ReviewDTO::toModel)
                .collect(Collectors.toSet())
        );

        return trainingCourseInfoDTO;
    }
}
