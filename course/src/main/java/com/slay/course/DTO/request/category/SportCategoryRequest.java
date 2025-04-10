package com.slay.course.DTO.request.category;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SportCategoryRequest {
    @Size(min = 3, max = 20, message = "The name must contain from 3 to 20 characters.")
    private String name;

    @Size(max = 1000, message = "The maximum number of characters is 1000.")
    private String description;

}
