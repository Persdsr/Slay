package com.slay.complaint.service.permission;

import com.slay.complaint.entity.ComplaintEntity;
import com.slay.complaint.exception.ComplaintNotFoundException;
import com.slay.complaint.repository.ComplaintRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ComplaintPermissionService {

    private final ComplaintRepo complaintRepo;

    public boolean isComplaintSender(int complaintId, int userId) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> ComplaintNotFoundException.builder().build());

        return complaint.getSenderId() == userId;
    }

}