package com.slay.complaint.service.complaint;

import com.slay.complaint.dto.request.complaint.ComplaintCourseRequest;
import com.slay.complaint.dto.response.complaint.ComplaintCourseDTO;
import com.slay.complaint.entity.ComplaintCourseEntity;
import com.slay.complaint.entity.ComplaintEntity;
import com.slay.complaint.enums.ComplaintType;
import com.slay.complaint.enums.CourseComplaintType;
import com.slay.complaint.exception.ComplaintNotFoundException;
import com.slay.complaint.repository.ComplaintCourseRepo;
import com.slay.complaint.repository.ComplaintRepo;
import com.slay.complaint.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComplaintCourseService {
    private final ComplaintRepo complaintRepo;
    private final ComplaintCourseRepo complaintCourseRepo;

    @Cacheable(value = "complaint-course-detail", key = "#complaintId")
    public ComplaintCourseDTO getComplaintCourseByComplaintId(int complaintId) {
        ComplaintEntity complaint = complaintRepo.findById(complaintId).orElseThrow(
                () -> ComplaintNotFoundException.builder().build()
        );

        ComplaintCourseEntity courseComplaint = (ComplaintCourseEntity) complaint;

        return ComplaintCourseDTO.toModel(courseComplaint);
    }

    @Transactional
    @CacheEvict(value = "complaint-user", key = "#userDetails.username")
    public void createCourseComplaint(UserDetailsImpl userDetails, ComplaintCourseRequest courseComplaintDTO) {
        ComplaintCourseEntity complaintUserProfileEntity = new ComplaintCourseEntity();
        complaintUserProfileEntity.setCourseComplaintType(courseComplaintDTO.getCourseComplaintType());
        complaintUserProfileEntity.setComplaintType(ComplaintType.COURSE);
        complaintUserProfileEntity.setReportedCourseId(courseComplaintDTO.getReportedCourse());
        complaintUserProfileEntity.setSenderId(userDetails.getId());
        complaintUserProfileEntity.setDescription(courseComplaintDTO.getDescription());

        complaintCourseRepo.save(complaintUserProfileEntity);
    }

    @Cacheable(value = "complaint-course-types")
    public Map<String, String> getComplaintCourseTypes() {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        return Arrays.stream(CourseComplaintType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        type -> bundle.getString(type.name())
                ));
    }


}
