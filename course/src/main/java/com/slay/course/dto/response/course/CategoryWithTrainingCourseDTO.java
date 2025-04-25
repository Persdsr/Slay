package com.slay.course.dto.response.course;

import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.entity.category.SportCategoryEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CategoryWithTrainingCourseDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private String poster;
    private List<TrainingCourseLiteDTO> trainingCourses;

    public static CategoryWithTrainingCourseDTO toModel(SportCategoryEntity category) {
        CategoryWithTrainingCourseDTO categoryDTO = new CategoryWithTrainingCourseDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setId(category.getId());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setPoster(category.getPoster());
        categoryDTO.setTrainingCourses(category.getTrainingCourses()
                .stream()
                        .sorted(Comparator.comparingInt(course -> -course.getBuyers().size()))
                        .limit(5)
                .map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList()));

        return categoryDTO;
    }

    public static CategoryWithTrainingCourseDTO toModel(SportCategoryEntity category, Map<Integer, UserLiteDTO> authorMap) {
        CategoryWithTrainingCourseDTO dto = new CategoryWithTrainingCourseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setPoster(category.getPoster());

        dto.setTrainingCourses(
                category.getTrainingCourses().stream()
                        .sorted(Comparator.comparingInt(course -> -course.getBuyers().size()))
                        .limit(5)
                        .map(course -> TrainingCourseLiteDTO.toModel(course, authorMap))
                        .collect(Collectors.toList())
        );

        return dto;
    }

}
