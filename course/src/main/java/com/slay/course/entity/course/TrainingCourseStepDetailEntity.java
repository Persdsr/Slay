package com.slay.course.entity.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "training_course_step_detail")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCourseStepDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 700, message = "Title must not exceed 1000 characters.")
    private String title;

    @ManyToOne()
    @JoinColumn(name = "training_course_step")
    private TrainingCourseStepEntity trainingCourseStep;

    @Size(max = 1500, message = "Description must not exceed 700 characters.")
    private String description;

    private String video;
}
