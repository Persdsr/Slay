package com.slay.course.service.permission;

import com.slay.course.entity.course.ReviewEntity;
import com.slay.course.exception.ReviewNotFoundException;
import com.slay.course.repository.course.TrainingCourseReviewRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReviewPermissionService {

    private final TrainingCourseReviewRepo reviewRepo;

    public boolean isReviewAuthor(int reviewId, String username) {

        ReviewEntity review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.builder().build());

        return review.getAuthor().equals(username);
    }

}