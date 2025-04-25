package com.slay.support.entity.support;

import com.slay.support.enums.SupportRequestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "support_request")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_id")
    private int sender;

    @NotNull(message = "Email is required.")
    private String email;

    @NotNull(message = "Subject is required.")
    private String subject;

    @OneToMany(mappedBy = "supportRequest", cascade = CascadeType.ALL)
    private List<SupportRequestMessageEntity> messages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Request type is required.")
    private SupportRequestType requestType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    private boolean resolved = false;

    @Column(name = "resolver_id")
    private int resolvedBy;

}