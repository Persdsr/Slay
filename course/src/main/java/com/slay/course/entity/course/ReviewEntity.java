package com.slay.course.entity.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "review")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 1000, message = "The maximum number of characters is 1000.")
    private String text;

    @Size(min = 2, max = 200, message = "Name must contain from 2 to 200 characters.")
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Min(1)
    @Max(5)
    private int rating;

    @NotNull(message = "Author id is required")
    private int authorId;

    @CollectionTable(name = "review_images")
    @ElementCollection
    private List<String> images;

    @ManyToOne()
    @JoinColumn(name = "training_course_id")
    @NotNull(message = "Training course is required")
    private TrainingCourseEntity trainingCourse;

}
