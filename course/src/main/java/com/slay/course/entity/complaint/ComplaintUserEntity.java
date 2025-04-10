package com.slay.course.entity.complaint;

import com.slay.course.entity.user.UserEntity;
import com.slay.course.enums.UserComplaintType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "complaint_user_profile")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintUserEntity extends ComplaintEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity reportedUser;

    @Column(name = "banned")
    private boolean banned = false;

    @Enumerated(EnumType.STRING)
    private UserComplaintType userComplaintType;
}
