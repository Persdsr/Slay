package com.slay.course.DTO.request.auth;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}