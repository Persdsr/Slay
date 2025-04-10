package com.slay.course.service.complaint;

import com.slay.course.DTO.request.complaint.ComplaintCourseRequest;
import com.slay.course.DTO.response.complaint.ComplaintCourseDTO;
import com.slay.course.entity.complaint.ComplaintCourseEntity;
import com.slay.course.entity.complaint.ComplaintEntity;
import com.slay.course.enums.ComplaintType;
import com.slay.course.enums.CourseComplaintType;
import com.slay.course.exception.ComplaintNotFoundException;
import com.slay.course.exception.TrainingCourseNotFoundException;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.complaint.ComplaintCourseRepo;
import com.slay.course.repository.complaint.ComplaintRepo;
import com.slay.course.repository.course.TrainingCourseRepo;
import com.slay.course.repository.user.UserRepo;
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
    private final UserRepo userRepo;
    private final ComplaintCourseRepo complaintCourseRepo;
    private final TrainingCourseRepo trainingCourseRepo;

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
    public void createCourseComplaint(UserDetails userDetails, ComplaintCourseRequest courseComplaintDTO) {
        ComplaintCourseEntity complaintUserProfileEntity = new ComplaintCourseEntity();
        complaintUserProfileEntity.setCourseComplaintType(courseComplaintDTO.getCourseComplaintType());
        complaintUserProfileEntity.setComplaintType(ComplaintType.COURSE);
        complaintUserProfileEntity.setReportedCourse(trainingCourseRepo.findById(courseComplaintDTO.getReportedCourse()).orElseThrow(
                () -> TrainingCourseNotFoundException.builder().build()
        ));
        complaintUserProfileEntity.setSender(userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        ));
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
