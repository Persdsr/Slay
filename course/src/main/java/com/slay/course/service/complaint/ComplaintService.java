package com.slay.course.service.complaint;

import com.slay.course.DTO.response.complaint.ComplaintCourseDTO;
import com.slay.course.DTO.response.complaint.ComplaintDTO;
import com.slay.course.DTO.response.complaint.ComplaintUserDTO;
import com.slay.course.entity.complaint.ComplaintCourseEntity;
import com.slay.course.entity.complaint.ComplaintEntity;
import com.slay.course.entity.complaint.ComplaintUserEntity;
import com.slay.course.enums.ComplaintType;
import com.slay.course.exception.ComplaintNotFoundException;
import com.slay.course.repository.complaint.ComplaintRepo;
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

    @Cacheable(value = "complaint-user", key = "#username")
    public List<ComplaintDTO> getAllUserComplaintsRequests(String username) {
        return complaintRepo.findAllBySenderUsername(username).stream()
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

        cacheManager.getCache("complaint-user").evict(complaint.getSender().getUsername());
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
