package com.slay.course.service.course;

import com.slay.course.dto.request.course.ReviewRequest;
import com.slay.course.entity.course.ReviewEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.exception.ReviewNotFoundException;
import com.slay.course.exception.TrainingCourseNotFoundException;
import com.slay.course.repository.course.TrainingCourseRepo;
import com.slay.course.repository.course.TrainingCourseReviewRepo;
import com.slay.course.service.file.FileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TrainingCourseReviewService {

    private final TrainingCourseRepo trainingCourseRepo;
    private final TrainingCourseReviewRepo reviewRepo;
    private final FileService fileService;

    public void createReview(UserDetails userDetails, ReviewRequest reviewDTO, int trainingCourseId, MultipartFile[] images) {
        TrainingCourseEntity trainingCourse = trainingCourseRepo.findById(trainingCourseId).orElseThrow(
                () -> TrainingCourseNotFoundException.builder().build()
        );

        List<String> savedFiles = (images != null && images.length > 0)
                ? fileService.saveAllFileList(images)
                : new ArrayList<>();

        ReviewEntity review = new ReviewEntity();
        review.setText(reviewDTO.getText());
        review.setImages(savedFiles);
        review.setTitle(reviewDTO.getTitle());
        review.setTrainingCourse(trainingCourse);
        review.setRating(reviewDTO.getRating());
       /* review.setAuthor(userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        ));*/

        reviewRepo.save(review);
    }

    public void updateReviewFields(int id, Map<String, Object> fields) {
        ReviewEntity review = reviewRepo.findById(id).orElseThrow(
                () -> ReviewNotFoundException.builder().build()
        );

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(ReviewEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, review, value);
        });

        reviewRepo.save(review);
    }

    public void deleteReviewById(int reviewId) {
        reviewRepo.deleteById(reviewId);
    }
}
