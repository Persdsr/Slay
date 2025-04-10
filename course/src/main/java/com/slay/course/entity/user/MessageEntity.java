package com.slay.course.entity.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "messages")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1500)
    private String message;

    @ManyToOne()
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ElementCollection
    @CollectionTable(name = "message_files", joinColumns = @JoinColumn(name = "message_id"))
    @Nullable
    private List<String> files;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;
}
