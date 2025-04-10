package com.slay.course.DTO.request.complaint;

import com.slay.course.enums.UserComplaintType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintUserRequest {
    @NotBlank(message = "Reported user is required")
    private String reportedUsername;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Complaint type is required")
    private UserComplaintType userComplaintType;

}
