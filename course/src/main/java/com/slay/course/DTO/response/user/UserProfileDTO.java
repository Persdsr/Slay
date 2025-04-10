package com.slay.course.DTO.response.user;

import com.slay.course.DTO.response.course.TrainingCourseInfoDTO;
import com.slay.course.DTO.response.review.ReviewDTO;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.entity.user.UserEntity;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserProfileDTO implements Serializable {
    private UserDTO author;
    private List<TrainingCourseInfoDTO> courses;
    private boolean isSubscribed;
    private boolean isBuyer;
    private Integer chatId;
    private List<ReviewDTO> reviews;
    private int reviewsCount;

    public static UserProfileDTO toModel(UserEntity userEntity) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setAuthor(UserDTO.toModel(userEntity));

        dto.setCourses(userEntity.getTrainingCourse().stream()
                .sorted(Comparator.comparing(TrainingCourseEntity::getCreatedAt).reversed())
                .map(course -> {
                    Hibernate.initialize(course.getReviews());
                    course.getReviews().forEach(review -> Hibernate.initialize(review.getImages()));
                    return TrainingCourseInfoDTO.toModel(course);
                })
                .collect(Collectors.toList()));

        List<ReviewDTO> allReviews = dto.getCourses().stream()
                .flatMap(course -> course.getReviews().stream())
                .collect(Collectors.toList());

        Collections.shuffle(allReviews);
        dto.setReviewsCount(allReviews.size());
        dto.setReviews(allReviews.stream().limit(5).collect(Collectors.toList()));

        return dto;
    }
}
