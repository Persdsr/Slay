package com.slay.user.client;

import com.slay.user.dto.request.course.TrainingCourseInfoDTO;
import com.slay.user.dto.response.course.TrainingCourseLiteDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseServiceClientFallback implements CourseServiceClient {
    @Override
    public List<TrainingCourseInfoDTO> getUserTrainingCourses(Integer authorId) {
        return null;
    }

    @Override
    public List<TrainingCourseLiteDTO> getTrainingCoursesByIds(List<Integer> ids) {
        return null;
    }
}
