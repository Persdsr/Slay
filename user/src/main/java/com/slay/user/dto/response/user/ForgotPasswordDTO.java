package com.slay.user.dto.response.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ForgotPasswordDTO implements Serializable {
    private String email;
    private String resetUrl;
    private String htmlContent;
    private String subject;
}
