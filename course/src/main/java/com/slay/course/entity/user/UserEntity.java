package com.slay.course.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slay.course.entity.complaint.ComplaintUserEntity;
import com.slay.course.entity.course.ReviewEntity;
import com.slay.course.entity.course.TrainingCourseEntity;
import com.slay.course.entity.support.SupportRequestEntity;
import com.slay.course.entity.support.SupportRequestMessageEntity;
import com.slay.course.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity(name = "users")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 4, max = 26, message = "The username must contain from 4 to 26 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The user name must have only Latin letters and numbers.")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @Email(message = "Email does not meet the requirements")
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "\\d+", message = "The field should contain only numbers.")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Past(message = "A birthday can't be in the future")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime  birthday;

    @Size(min = 2, max = 70, message = "The name must contain from 2 to 70 characters.")
    private String name;

    private String banner;

    @Column(name = "last_login")
    private Date lastLogin;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Size(max = 1000, message = "The maximum number of characters is 500")
    @Column(name = "about_me")
    private String aboutMe;

    private String avatar;

    @Size(max = 100, message = "The maximum number of characters is 100")
    private String slogan;

    @Column(name = "is_confirmed")
    private boolean isConfirmed = false;

    @Column(name = "is_banned")
    private boolean isBanned = false;

    private String country;

    private String language;

    @OneToMany(mappedBy = "author")
    private List<TrainingCourseEntity> trainingCourse;

    @ManyToMany(mappedBy = "courseBuyers")
    private Set<TrainingCourseEntity> purchasedTrainingCourses;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToMany()
    @JoinTable(name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<UserEntity> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    private Set<UserEntity> subscribers;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<ReviewEntity> reviews;

    @ManyToMany()
    @JoinTable(name = "user_favorite_training_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_course_id"))
    private List<TrainingCourseEntity> favoriteTrainingCourses;

    @OneToMany(mappedBy = "sender")
    private List<SupportRequestEntity> supportRequest;

    @OneToMany(mappedBy = "resolvedBy")
    private List<SupportRequestEntity> resolvedSupportRequests;

    @OneToMany(mappedBy = "sender")
    private List<SupportRequestMessageEntity> supportRequestMessages;

    @OneToMany(mappedBy = "reportedUser")
    private Set<ComplaintUserEntity> complaints;

    @ManyToMany()
    @JoinTable(
            name = "user_chats",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<ChatEntity> chats = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<MessageEntity> messages = new HashSet<>();

}

