package com.slay.complaint.dto.request.complaint;

import com.slay.complaint.enums.CourseComplaintType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintCourseRequest {
    @NotNull(message = "Reported course is required")
    private int reportedCourse;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Complaint type is required")
    private CourseComplaintType courseComplaintType;
}
