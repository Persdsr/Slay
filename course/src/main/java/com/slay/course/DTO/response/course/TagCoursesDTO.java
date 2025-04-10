package com.slay.course.DTO.response.course;

import com.slay.course.entity.category.TagEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TagCoursesDTO implements Serializable {
    private int id;
    private String name;
    private Set<TrainingCourseLiteDTO> trainingCourses;

    public static TagCoursesDTO toModel(TagEntity tagEntity) {
        TagCoursesDTO tagCoursesDTO = new TagCoursesDTO();
        tagCoursesDTO.setName(tagEntity.getName());
        tagCoursesDTO.setId(tagEntity.getId());
        tagCoursesDTO.setTrainingCourses(tagEntity.getTrainingCourses().stream()
                        .sorted((course1, course2) -> course2.getCreatedAt().compareTo(course1.getCreatedAt()))
                .map(TrainingCourseLiteDTO::toModel)
                .collect(Collectors.toSet())
        );

        return tagCoursesDTO;
    }
}
