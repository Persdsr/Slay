package com.slay.course.service.user;

import com.slay.course.DTO.response.course.TrainingCourseLiteDTO;
import com.slay.course.DTO.response.user.UserProfileDTO;
import com.slay.course.DTO.response.user.UserProfileSettingDTO;
import com.slay.course.entity.user.ChatEntity;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.UserNotFoundException;
import com.slay.course.repository.user.ChatRepo;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final FileService fileService;
    private final ChatRepo chatRepo;

    @Cacheable(value = "user-profile", key = "#profileUsername")
    public UserProfileDTO getAuthorProfile(UserDetails userDetails, String profileUsername) {
        UserEntity profileUser = userRepo.findByUsername(profileUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserProfileDTO userProfileDTO = UserProfileDTO.toModel(profileUser);

        if (userDetails != null && !profileUsername.equals(userDetails.getUsername())) {
            UserEntity currentUser = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                    () -> UserNotFoundException.builder().build()
            );

            boolean isSubscribed = currentUser.getSubscriptions().contains(profileUser);
            userProfileDTO.setSubscribed(isSubscribed);
            userProfileDTO.setBuyer(currentUser.getPurchasedTrainingCourses().stream()
                    .anyMatch(course -> profileUser.getTrainingCourse().contains(course)) && !currentUser.getUsername().equals(userProfileDTO.getAuthor().getUsername()));
            Optional<ChatEntity> existingChat = chatRepo.findChatByMembers(currentUser
                    , profileUser);

            if (existingChat.isPresent()) {
                userProfileDTO.setChatId(existingChat.get().getId());
            } else {
                userProfileDTO.setChatId(null);
            }

        } else {
            userProfileDTO.setSubscribed(false);
            userProfileDTO.setBuyer(false);
        }

        return userProfileDTO;
    }

    @Cacheable(value = "profile-settings-data", key = "#userDetails.username")
    public UserProfileSettingDTO getProfileSettingsData(UserDetails userDetails) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        return UserProfileSettingDTO.toModel(user);
    }

    @Cacheable(value = "user-purchases-courses", key = "#userDetails.username")
    public List<TrainingCourseLiteDTO> getUserPurchaseCourses(UserDetails userDetails) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        return user.getPurchasedTrainingCourses().stream().map(TrainingCourseLiteDTO::toModel).collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "user-profile", key = "#authorUsername")
    public void followToUser(UserDetails userDetails, String authorUsername) {
        UserEntity follower = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity author = userRepo.findByUsername(authorUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        follower.getSubscriptions().add(author);
        author.getSubscribers().add(follower);
        userRepo.save(author);
        userRepo.save(follower);
    }

    @Transactional
    @CacheEvict(value = "user-profile", key = "#authorUsername")
    public void unfollowUser(UserDetails userDetails, String authorUsername) {
        UserEntity follower = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity author = userRepo.findByUsername(authorUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        follower.getSubscriptions().remove(author);
        userRepo.save(author);
        userRepo.save(follower);
    }
    
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "profile-settings-data", key = "#userDetails.username"),
            @CacheEvict(value = "user-profile", key = "#userDetails.username")
    })
    public void updateUserByFields(UserDetails userDetails, Map<String, Object> fields, MultipartFile avatar, MultipartFile banner) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserEntity.class, key);
            if (field != null) {
                field.setAccessible(true);
                Object newValue = value;

                if (field.getType().equals(LocalDateTime.class) && value instanceof String) {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    newValue = LocalDateTime.parse((String) value, formatter);
                }

                ReflectionUtils.setField(field, user, newValue);
            }
        });

        if (avatar != null) {
            user.setAvatar(fileService.saveFile(avatar));
        }
        if (banner != null) {
            user.setBanner(fileService.saveFile(banner));
        }

        userRepo.save(user);
    }
}
