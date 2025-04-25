package com.slay.complaint.entity;

import com.slay.complaint.enums.ComplaintType;
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

    private int senderId;

    @NotNull(message = "Description is required")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Complaint type is required")
    private ComplaintType complaintType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private boolean isResolved = false;

    private int resolvedBy;
}