package com.slay.course.service.complaint;

import com.slay.course.DTO.request.complaint.ComplaintUserRequest;
import com.slay.course.DTO.response.complaint.ComplaintUserDTO;
import com.slay.course.entity.complaint.ComplaintEntity;
import com.slay.course.entity.complaint.ComplaintUserEntity;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.enums.ComplaintType;
import com.slay.course.exception.ComplaintNotFoundException;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.complaint.ComplaintRepo;
import com.slay.course.repository.complaint.ComplaintUserRepo;
import com.slay.course.repository.user.UserRepo;
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
    private final UserRepo userRepo;
    private final ComplaintRepo complaintRepo;

    public void createUserComplaint(UserDetails userDetails, ComplaintUserRequest complaintUserRequest) {
        ComplaintUserEntity complaintUserEntity = new ComplaintUserEntity();
        complaintUserEntity.setComplaintType(ComplaintType.USER_PROFILE);
        complaintUserEntity.setReportedUser(userRepo.findByUsername(complaintUserRequest.getReportedUsername())
                .orElseThrow(() -> UserNotFoundException.builder().build()));
        complaintUserEntity.setSender(userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> UserNotFoundException.builder().build()));
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

    @Caching(evict = {
            @CacheEvict(value = "complaint-user-detail", key = "#complaintUserId")
    })
    public void changeResolveBanStatus(int complaintUserId, boolean resolveBanStatus) {
        ComplaintUserEntity complaintUser = complaintUserRepo.findById(complaintUserId).orElseThrow(
                () -> ComplaintNotFoundException.builder().build()
        );

        complaintUser.setBanned(resolveBanStatus);
        complaintUser.setResolved(true);
        complaintUserRepo.save(complaintUser);

        if (resolveBanStatus) {
            UserEntity bannedUser = complaintUser.getReportedUser();
            bannedUser.setBanned(true);
            userRepo.save(bannedUser);
        }
    }
}
