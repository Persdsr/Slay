package com.slay.course.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMinimalInfoDTO {
    int id;
    String username;
}
