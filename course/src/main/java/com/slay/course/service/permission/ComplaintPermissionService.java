package com.slay.course.service.permission;

import com.slay.course.entity.complaint.ComplaintEntity;
import com.slay.course.exception.ComplaintNotFoundException;
import com.slay.course.repository.complaint.ComplaintRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ComplaintPermissionService {

    private final ComplaintRepo complaintRepo;

    public boolean isComplaintSender(int complaintId, String username) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> ComplaintNotFoundException.builder().build());

        return complaint.getSender().getUsername().equals(username);
    }


}