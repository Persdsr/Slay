package com.slay.complaint.entity;

import com.slay.complaint.enums.CourseComplaintType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "complaint_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintCourseEntity extends ComplaintEntity {

    @NotNull(message = "Reported user id is required")
    private int reportedCourseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_complaint_type")
    private CourseComplaintType courseComplaintType;
}