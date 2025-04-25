package com.slay.user.entity.user;

import com.slay.user.entity.chat.ChatEntity;
import com.slay.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 26, message = "Username must be between 4 and 26 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters and numbers")
    @Column(unique = true, nullable = false)
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    private String password;

    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Past(message = "Birthday must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Size(min = 2, max = 70, message = "Name must be between 2 and 70 characters")
    private String name;

    private String avatarUrl;

    private String bannerUrl;

    @Size(max = 1000, message = "About me cannot exceed 1000 characters")
    @Column(name = "about_me")
    private String aboutMe;

    @Size(max = 100, message = "Slogan cannot exceed 100 characters")
    private String slogan;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed = false;

    @Column(name = "is_banned", nullable = false)
    private boolean isBanned = false;

    private String country;

    private String language;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_subscriptions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "subscribed_to_id")
    private Set<Integer> subscribedToIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_favorite_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    private Set<Integer> favoriteCourseIds = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "user_chats",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<ChatEntity> chats = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_purchased_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    private List<Integer> purchasedCourseIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    private List<Integer> coursesIds = new ArrayList<>();
}