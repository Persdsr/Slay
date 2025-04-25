package com.slay.complaint.entity;

import com.slay.complaint.enums.UserComplaintType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private int reportedUserId;

    @Column(name = "banned")
    private boolean banned = false;

    @Enumerated(EnumType.STRING)
    private UserComplaintType userComplaintType;
}
