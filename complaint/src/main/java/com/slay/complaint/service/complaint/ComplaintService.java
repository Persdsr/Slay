package com.slay.complaint.service.complaint;

import com.slay.complaint.dto.response.complaint.ComplaintCourseDTO;
import com.slay.complaint.dto.response.complaint.ComplaintDTO;
import com.slay.complaint.dto.response.complaint.ComplaintUserDTO;
import com.slay.complaint.entity.ComplaintCourseEntity;
import com.slay.complaint.entity.ComplaintEntity;
import com.slay.complaint.entity.ComplaintUserEntity;
import com.slay.complaint.enums.ComplaintType;
import com.slay.complaint.exception.ComplaintNotFoundException;
import com.slay.complaint.repository.ComplaintRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComplaintService {
    private final ComplaintRepo complaintRepo;
    private final CacheManager cacheManager;

    public List<ComplaintDTO> getAllComplaints() {
        return complaintRepo.findAll().stream().map(ComplaintDTO::toModel).collect(Collectors.toList());
    }

    public ResponseEntity getComplaintById(int id) {
        ComplaintEntity complaint = complaintRepo.findById(id).orElse(null);

        if (complaint.getComplaintType().name().equals(ComplaintType.COURSE.name())) {

            ComplaintCourseEntity complaintCourse = (ComplaintCourseEntity) complaint;

            return ResponseEntity.ok().body(ComplaintCourseDTO.toModel(complaintCourse));

        } else if (complaint.getComplaintType().name().equals(ComplaintType.USER_PROFILE.name())) {

            ComplaintUserEntity complaintUser = (ComplaintUserEntity) complaint;

            return ResponseEntity.ok().body(ComplaintUserDTO.toModel(complaintUser));
        }

        return ResponseEntity.notFound().build();
    }

    @Cacheable(value = "complaint-user", key = "#senderId")
    public List<ComplaintDTO> getAllUserComplaintsRequests(int senderId) {
        return complaintRepo.findAllBySenderId(senderId).stream()
                .map(ComplaintDTO::toModel)
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "complaint-detail", key = "#complaintId"),
            @CacheEvict(value = "complaint-course-detail", key = "#complaintId")
    })
    public void changeComplaintResolvedStatus(int complaintId) {
        ComplaintEntity complaint = complaintRepo.findById(complaintId).orElseThrow(
                () -> ComplaintNotFoundException.builder().build()
        );
        complaint.setResolved(!complaint.isResolved());
        complaintRepo.save(complaint);

        cacheManager.getCache("complaint-user").evict(complaint.getSenderId());
    }

    @Cacheable(value = "complaint-types")
    public Map<String, String> getComplaintTypes() {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        return Arrays.stream(ComplaintType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        type -> bundle.getString(type.name())
                ));
    }
}
