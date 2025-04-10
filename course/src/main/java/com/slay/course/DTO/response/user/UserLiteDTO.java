package com.slay.course.DTO.response.user;

import com.slay.course.entity.user.UserEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLiteDTO implements Serializable {
    private String username;
    private String avatar;

    public static UserLiteDTO toModel(UserEntity userEntity) {
        UserLiteDTO userDTO = new UserLiteDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setAvatar(userEntity.getAvatar());
        return userDTO;
    }
}
