package com.slay.course.entity.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "training_course_steps")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCourseStepEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 700, message = "The title must not exceed 700 characters.")
    private String title;

    @Size(max = 1000, message = "The description must not exceed 1000 characters.")
    private String description;

    @OneToMany(mappedBy = "trainingCourseStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingCourseStepDetailEntity> trainingCourseStepDetails = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "training_course_id")
    private TrainingCourseEntity trainingCourse;

    public void addDetail(TrainingCourseStepDetailEntity detail) {
        trainingCourseStepDetails.add(detail);
        detail.setTrainingCourseStep(this);
    }


}
