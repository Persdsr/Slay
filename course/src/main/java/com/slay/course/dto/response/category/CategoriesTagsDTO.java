package com.slay.course.dto.response.category;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class CategoriesTagsDTO implements Serializable {
    private List<SportCategoryDTO> categories;
    private Set<String> tags;

}
