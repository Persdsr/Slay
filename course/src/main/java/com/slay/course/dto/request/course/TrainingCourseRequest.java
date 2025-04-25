package com.slay.course.dto.request.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
public class TrainingCourseRequest {

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than or equal to 1.")
    private int price;

    @NotNull(message = "Category is required")
    private String category;

    @NotNull(message = "Tags are required")
    private Set<String> tags;

    @NotNull(message = "Training course steps are required")
    private List<TrainingCourseStepDto> trainingCourseSteps;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainingCourseStepDto {
        @NotNull(message = "Step title is required")
        @Size(max = 700, message = "Step title must not exceed 700 characters.")
        private String title;

        @Size(max = 1000, message = "Step description must not exceed 1000 characters.")
        private String description;

        private List<TrainingCourseStepDetailDto> trainingCourseStepDetails;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainingCourseStepDetailDto {
        @NotNull(message = "Detail title is required")
        @Size(max = 700, message = "Detail title must not exceed 700 characters.")
        private String title;

        @Size(max = 1500, message = "Detail description must not exceed 1500 characters.")
        private String description;

        private String video;
    }
}