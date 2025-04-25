package com.slay.user.dto.response.user;

import com.slay.user.entity.user.UserEntity;
import com.slay.user.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO implements Serializable {
    private String username;
    private String name;
    private String email;
    private LocalDateTime lastLogin;
    private String aboutMe;
    private String banner;
    private String avatar;
    private boolean isConfirmed;
    private boolean isBanned;
    private Set<Role> roles;
    private int followersCount;

    public static UserDTO toModel(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setName(userEntity.getName());
        userDTO.setLastLogin(userEntity.getLastLogin());
        userDTO.setAboutMe(userEntity.getAboutMe());
        userDTO.setBanner(userEntity.getBannerUrl());
        userDTO.setAvatar(userEntity.getAvatarUrl());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setConfirmed(userEntity.isConfirmed());
        userDTO.setFollowersCount(userEntity.getSubscribedToIds().size());
        userDTO.setBanned(userEntity.isBanned());
        userDTO.setRoles(userEntity.getRoles());

        return userDTO;
    }

}
