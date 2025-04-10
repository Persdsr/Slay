package com.slay.course.DTO.response.review;

import com.slay.course.DTO.response.course.TrainingCourseMinimaInfo;
import com.slay.course.DTO.response.user.UserDTO;
import com.slay.course.entity.course.ReviewEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReviewDTO implements Serializable {
    private int id;
    private String title;
    private String text;
    private int rating;
    private UserDTO author;
    private TrainingCourseMinimaInfo course;
    private List<String> images;

    public static ReviewDTO toModel(ReviewEntity reviewEntity) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(reviewEntity.getId());
        reviewDTO.setTitle(reviewEntity.getTitle());
        reviewDTO.setText(reviewEntity.getText());
        reviewDTO.setRating(reviewEntity.getRating());
        reviewDTO.setImages(reviewEntity.getImages());
        reviewDTO.setAuthor(UserDTO.toModel(reviewEntity.getAuthor()));
        reviewDTO.setCourse(TrainingCourseMinimaInfo.toModel(reviewEntity.getTrainingCourse()));

        return reviewDTO;
    }

}
