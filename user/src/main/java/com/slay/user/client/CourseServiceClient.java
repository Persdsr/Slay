package com.slay.user.client;

import com.slay.user.config.FeignClientConfig;
import com.slay.user.dto.request.course.TrainingCourseInfoDTO;
import com.slay.user.dto.response.course.TrainingCourseLiteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "course-service",
        url = "${course.service.url}",
        configuration = FeignClientConfig.class,
        fallback = CourseServiceClientFallback.class
)
@Primary
public interface CourseServiceClient {

    @GetMapping("/api/training-course/author")
    List<TrainingCourseInfoDTO> getUserTrainingCourses(@RequestParam("authorId") Integer authorId);

    @GetMapping("/api/training-course/courses-by-ids")
    List<TrainingCourseLiteDTO> getTrainingCoursesByIds(@RequestParam("ids") List<Integer> ids);
}