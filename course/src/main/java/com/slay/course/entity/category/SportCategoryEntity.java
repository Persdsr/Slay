package com.slay.course.entity.category;

import com.slay.course.entity.course.TrainingCourseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity(name = "sport_category")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportCategoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 20, message = "The name must contain from 3 to 20 characters.")
    private String name;

    @Size(max = 1000, message = "The maximum number of characters is 1000.")
    private String description;

    @NotNull(message = "The poster is required")
    private String poster;

    @OneToMany(mappedBy = "category")
    private List<TrainingCourseEntity> trainingCourses;

}
