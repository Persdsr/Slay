package com.slay.course.DTO.response.user;

import com.slay.course.entity.user.UserEntity;
import com.slay.course.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class UserDTO implements Serializable {
    private String username;
    private String name;
    private String email;
    private Date lastLogin;
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
        userDTO.setBanner(userEntity.getBanner());
        userDTO.setAvatar(userEntity.getAvatar());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setConfirmed(userEntity.isConfirmed());
        userDTO.setFollowersCount(userEntity.getSubscribers().size());
        userDTO.setBanned(userEntity.isBanned());
        userDTO.setRoles(userEntity.getRoles());

        return userDTO;
    }

}
