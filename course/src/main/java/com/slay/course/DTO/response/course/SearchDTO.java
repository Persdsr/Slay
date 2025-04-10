package com.slay.course.DTO.response.course;

import com.slay.course.DTO.response.user.UserLiteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchDTO implements Serializable {
    private List<TagCoursesDTO> tags;
    private List<CategoryWithTrainingCourseDTO> categories;
    private List<UserLiteDTO> authors;
    private List<TrainingCourseLiteDTO> courses;

}
