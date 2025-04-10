package com.slay.course.DTO.response.user;

import com.slay.course.entity.user.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserProfileSettingDTO implements Serializable {

    private String username;
    private String avatar;
    private String banner;
    private String aboutMe;
    private String name;
    private LocalDateTime birthday;

    public static UserProfileSettingDTO toModel(UserEntity userEntity) {
        UserProfileSettingDTO userDTO = new UserProfileSettingDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setAvatar(userEntity.getAvatar());
        userDTO.setBanner(userEntity.getBanner());
        userDTO.setAboutMe(userEntity.getAboutMe());
        userDTO.setBirthday(userEntity.getBirthday());
        userDTO.setName(userEntity.getName());
        return userDTO;
    }
}
