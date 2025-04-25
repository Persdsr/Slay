package com.slay.user.dto.response.user;

import com.slay.user.dto.request.course.TrainingCourseInfoDTO;
import com.slay.user.dto.request.review.ReviewDTO;
import com.slay.user.entity.user.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

        /*dto.setCourses(userEntity.getTrainingCourse().stream()
                .sorted(Comparator.comparing(TrainingCourseEntity::getCreatedAt).reversed())
                .map(course -> {
                    Hibernate.initialize(course.getReviews());
                    course.getReviews().forEach(review -> Hibernate.initialize(review.getImages()));
                    return TrainingCourseInfoDTO.toModel(course);
                })
                .collect(Collectors.toList()));*/

        return dto;
    }
}
