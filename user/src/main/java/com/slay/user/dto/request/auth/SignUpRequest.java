package com.slay.user.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest implements Serializable {

    @Size(min = 4, max = 26, message = "The username must contain from 4 to 26 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The username must have only Latin letters and numbers.")
    private String username;

    @Email(message = "The email does not meet the requirements")
    @Size(max = 100, message = "The email must be less than 100 characters.")
    private String email;

    @Size(min = 2, max = 70, message = "The name must contain from 2 to 70 characters.")
    private String name;

    @Size(min = 8, message = "The password must be at least 8 characters long.")
    private String password;
}