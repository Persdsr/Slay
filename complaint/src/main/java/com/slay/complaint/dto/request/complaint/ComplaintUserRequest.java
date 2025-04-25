package com.slay.complaint.dto.request.complaint;

import com.slay.complaint.enums.UserComplaintType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintUserRequest {
    @NotBlank(message = "Reported user id is required")
    private int reportedUserId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Complaint type is required")
    private UserComplaintType userComplaintType;

}
