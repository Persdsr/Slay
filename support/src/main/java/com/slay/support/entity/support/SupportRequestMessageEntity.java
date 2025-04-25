package com.slay.support.entity.support;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "support_request_message")
public class SupportRequestMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 5000)
    private String message;

    @Column(name = "sender_id")
    @Nullable
    private int sender;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "support_message_images", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "message_images")
    private Set<String> images;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "support_request_id")
    private SupportRequestEntity supportRequest;

}
