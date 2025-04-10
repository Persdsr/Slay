package com.slay.course.entity.complaint;

import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.enums.CourseComplaintType;
import jakarta.persistence.*;
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

    @ManyToOne()
    @JoinColumn(name = "course_id", nullable = false)
    private TrainingCourseEntity reportedCourse;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_complaint_type")
    private CourseComplaintType courseComplaintType;
}