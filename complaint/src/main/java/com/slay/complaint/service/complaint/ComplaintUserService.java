package com.slay.complaint.service.complaint;

import com.slay.complaint.dto.request.complaint.ComplaintUserRequest;
import com.slay.complaint.dto.response.complaint.ComplaintUserDTO;
import com.slay.complaint.entity.ComplaintEntity;
import com.slay.complaint.entity.ComplaintUserEntity;
import com.slay.complaint.enums.ComplaintType;
import com.slay.complaint.exception.ComplaintNotFoundException;
import com.slay.complaint.repository.ComplaintRepo;
import com.slay.complaint.repository.ComplaintUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComplaintUserService {
    private final ComplaintUserRepo complaintUserRepo;
    private final ComplaintRepo complaintRepo;

    public void createUserComplaint(int userId, ComplaintUserRequest complaintUserRequest) {
        ComplaintUserEntity complaintUserEntity = new ComplaintUserEntity();
        complaintUserEntity.setComplaintType(ComplaintType.USER_PROFILE);
        complaintUserEntity.setReportedUserId(complaintUserRequest.getReportedUserId());
        complaintUserEntity.setSenderId(userId);
        complaintUserEntity.setDescription(complaintUserRequest.getDescription());

        complaintUserRepo.save(complaintUserEntity);

    }

    @Cacheable(value = "complaint-user-detail", key = "#complaintId")
    public ComplaintUserDTO getComplaintUserByComplaintId(int complaintId) {
        ComplaintEntity complaint = complaintRepo.findById(complaintId).orElseThrow(
                () -> ComplaintNotFoundException.builder().build()
        );

        try {
            ComplaintUserEntity userComplaint = (ComplaintUserEntity) complaint;
            return ComplaintUserDTO.toModel(userComplaint);

        } catch (Exception e) {
            throw ComplaintNotFoundException.builder().build();
        }

    }

    @Cacheable(value = "banned-users")
    public Set<ComplaintUserDTO> getBannedUsers() {
        return complaintUserRepo.findAllByBannedTrue().stream().map(ComplaintUserDTO::toModel).collect(Collectors.toSet());
    }
}
