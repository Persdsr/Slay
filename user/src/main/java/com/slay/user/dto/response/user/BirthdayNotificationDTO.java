package com.slay.user.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BirthdayNotificationDTO implements Serializable {
    private String email;
    private String name;
}