package com.slay.user.dto.response.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursePurchasedEvent {
    private Integer userId;
    private Integer courseId;
    private LocalDateTime purchasedAt;
}