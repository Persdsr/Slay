package com.slay.course.dto.response.course;

import com.slay.course.entity.category.SportCategoryEntity;
import com.slay.course.entity.category.TagEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CategoryAndPopularTagsDTO implements Serializable {
    private Set<TagCoursesDTO> tags;
    private List<CategoryWithTrainingCourseDTO> categories;

    public static CategoryAndPopularTagsDTO toModel(List<SportCategoryEntity> categories, List<TagEntity> tags) {
        List<CategoryWithTrainingCourseDTO> categoryDTOs = categories.stream()
                .map(CategoryWithTrainingCourseDTO::toModel)
                .collect(Collectors.toList());

        Set<TagCoursesDTO> tagDTOs = tags.stream()
                .map(TagCoursesDTO::toModel)
                .limit(2)
                .collect(Collectors.toSet());

        CategoryAndPopularTagsDTO dto = new CategoryAndPopularTagsDTO();
        dto.setCategories(categoryDTOs);
        dto.setTags(tagDTOs);

        return dto;
    }
}
