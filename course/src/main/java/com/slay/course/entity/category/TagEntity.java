package com.slay.course.entity.category;

import com.slay.course.entity.course.TrainingCourseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "tags")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "The name is required")
    @Size(min = 1, max = 30)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<TrainingCourseEntity> trainingCourses;

}
