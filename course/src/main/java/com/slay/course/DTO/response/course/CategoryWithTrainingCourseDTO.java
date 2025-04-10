package com.slay.course.DTO.response.course;

import com.slay.course.entity.category.SportCategoryEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
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
                        .sorted(Comparator.comparingInt(course -> -course.getCourseBuyers().size()))
                        .limit(5)
                .map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList()));

        return categoryDTO;
    }

}
