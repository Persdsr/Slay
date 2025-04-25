package com.slay.user.dto.request.auth;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}