package com.slay.course.entity.course;

import com.slay.course.entity.category.SportCategoryEntity;
import com.slay.course.entity.category.TagEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.*;

@Entity(name = "training_course")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCourseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 100)
    @NotNull(message = "Name is required")
    private String name;

    @Size(max = 1000, message = "The description must not exceed 1000 characters.")
    private String description;

    @NotNull(message = "Poster is required")
    private String poster;

    private String trailer;

    private int price = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @NotNull(message = "Author id is required")
    private int authorId;

    @ManyToOne()
    @JoinColumn(name = "sport_category_id")
    @NotNull(message = "Category is required")
    private SportCategoryEntity category;

    @ElementCollection
    @CollectionTable(
            name = "training_course_buyers",
            joinColumns = @JoinColumn(name = "training_course_id"))
    @Column(name = "user_id")
    private Set<Integer> buyers = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "training_course_tags",
            joinColumns = @JoinColumn(name = "training_course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags = new ArrayList<>();

    @OneToMany(mappedBy = "trainingCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingCourseStepEntity> trainingCourseSteps;

    @OneToMany(mappedBy = "trainingCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewEntity> reviews;

    @ElementCollection
    @CollectionTable(
            name = "course_favorites",
            joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "user_id")
    private Set<Integer> favoriteUsers = new HashSet<>();

    public void addStep(TrainingCourseStepEntity step) {
        if (this.trainingCourseSteps == null) {
            this.trainingCourseSteps = new ArrayList<>();
        }
        this.trainingCourseSteps.add(step);
        step.setTrainingCourse(this);
    }

}
