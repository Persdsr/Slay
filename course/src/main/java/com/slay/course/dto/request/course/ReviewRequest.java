package com.slay.course.dto.request.course;

import com.slay.course.entity.course.ReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewRequest implements Serializable {
    @Schema(description = "Текст отзыва", example = "Отличный курс! Очень полезный материал")
    @Size(max = 1000, message = "The maximum number of characters is 1000.")
    private String text;

    @Schema(description = "Заголовок отзыва", example = "Рекомендую")
    @NotBlank(message = "Text is required")
    @Size(min = 2, max = 200, message = "Name must contain from 2 to 200 characters.")
    private String title;

    @Schema(description = "Рейтинг курса", example = "5")
    @Min(1)
    @Max(5)
    private int rating;

    public static ReviewRequest toModel(ReviewEntity reviewEntity) {
        ReviewRequest dto = new ReviewRequest();
        dto.setText(reviewEntity.getText());
        dto.setTitle(reviewEntity.getTitle());
        dto.setRating(reviewEntity.getRating());
        return dto;
    }
}
