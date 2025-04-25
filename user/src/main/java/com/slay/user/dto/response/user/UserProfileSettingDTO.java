package com.slay.user.dto.response.user;

import com.slay.user.entity.user.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserProfileSettingDTO implements Serializable {

    private String username;
    private String avatar;
    private String banner;
    private String aboutMe;
    private String name;
    private LocalDate birthday;

    public static UserProfileSettingDTO toModel(UserEntity userEntity) {
        UserProfileSettingDTO userDTO = new UserProfileSettingDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setAvatar(userEntity.getAvatarUrl());
        userDTO.setBanner(userEntity.getBannerUrl());
        userDTO.setAboutMe(userEntity.getAboutMe());
        userDTO.setBirthday(userEntity.getBirthday());
        userDTO.setName(userEntity.getName());
        return userDTO;
    }
}
