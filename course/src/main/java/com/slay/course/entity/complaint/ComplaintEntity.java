package com.slay.course.entity.complaint;

import com.slay.course.entity.user.UserEntity;
import com.slay.course.enums.ComplaintType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "complaint")
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @NotNull(message = "Description is required")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Complaint type is required")
    private ComplaintType complaintType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private boolean isResolved = false;

    @ManyToOne
    @JoinColumn(name = "resolver_id")
    private UserEntity resolvedBy;
}