package com.slay.user.service.user;

import com.slay.user.client.CourseServiceClient;
import com.slay.user.dto.request.course.TrainingCourseInfoDTO;
import com.slay.user.dto.response.course.TrainingCourseLiteDTO;
import com.slay.user.dto.response.user.UserLiteDTO;
import com.slay.user.dto.response.user.UserProfileDTO;
import com.slay.user.dto.response.user.UserProfileSettingDTO;
import com.slay.user.entity.user.UserEntity;
import com.slay.user.security.services.UserDetailsImpl;
import com.slay.user.repository.UserRepo;
import com.slay.user.service.file.FileService;
import com.slay.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final FileService fileService;
    private final CourseServiceClient courseServiceClient;
    //private final ChatRepo chatRepo;

    //Cacheable(value = "user-profile", key = "#profileUsername")
    public UserProfileDTO getAuthorProfile(UserDetailsImpl userDetails, String profileUsername) {
        UserEntity profileUser = userRepo.findByUsername(profileUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserProfileDTO userProfileDTO = UserProfileDTO.toModel(profileUser);
        List<TrainingCourseInfoDTO> trainingCourses = courseServiceClient.getUserTrainingCourses(profileUser.getId());
        trainingCourses.forEach(course -> course.setAuthor(UserLiteDTO.toModel(userRepo.findByUsername(profileUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        ))));
        userProfileDTO.setCourses(trainingCourses);

        if (userDetails != null && !profileUsername.equals(userDetails.getUsername())) {
            UserEntity currentUser = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                    () -> UserNotFoundException.builder().build()
            );

            boolean isSubscribed = currentUser.getSubscribedToIds().contains(profileUser.getId());
            userProfileDTO.setSubscribed(isSubscribed);

            /*userProfileDTO.setBuyer(currentUser.getPurchasedTrainingCourses().stream()
                    .anyMatch(course -> profileUser.getTrainingCourse().contains(course)) && !currentUser.getUsername().equals(userProfileDTO.getAuthor().getUsername()));*/
            /*Optional<ChatEntity> existingChat = chatRepo.findChatByMembers(currentUser
                    , profileUser);*/

            /*if (existingChat.isPresent()) {
                userProfileDTO.setChatId(existingChat.get().getId());
            } else {
                userProfileDTO.setChatId(null);
            }*/

        } else {
            userProfileDTO.setSubscribed(false);
            userProfileDTO.setBuyer(false);
        }

        return userProfileDTO;
    }

    @Cacheable(value = "profile-settings-data", key = "#userDetails.username")
    public UserProfileSettingDTO getProfileSettingsData(UserDetailsImpl userDetails) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        return UserProfileSettingDTO.toModel(user);
    }

    @Cacheable(value = "user-lite-info", key = "#userId")
    public UserLiteDTO getUserLiteInfo(Integer userId) {
        UserEntity user = userRepo.findById(userId).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        return UserLiteDTO.toModel(user);
    }

    @Cacheable(value = "user-purchases-courses", key = "#userDetails.username")
    public List<TrainingCourseLiteDTO> getUserPurchaseCourses(UserDetailsImpl userDetails) {
        UserEntity user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        List<TrainingCourseLiteDTO> userCourses = courseServiceClient.getTrainingCoursesByIds(user.getPurchasedCourseIds());

        return userCourses;
    }

    @Transactional
    @CacheEvict(value = "user-profile", key = "#authorUsername")
    public void followToUser(UserDetailsImpl userDetails, String authorUsername) {
        UserEntity follower = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity author = userRepo.findByUsername(authorUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        follower.getSubscribedToIds().add(author.getId());
        userRepo.save(author);
        userRepo.save(follower);
    }

    @Transactional
    @CacheEvict(value = "user-profile", key = "#authorUsername")
    public void unfollowUser(UserDetailsImpl userDetails, String authorUsername) {
        UserEntity follower = userRepo.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );
        UserEntity author = userRepo.findByUsername(authorUsername).orElseThrow(
                () -> UserNotFoundException.builder().build()
        );

        follower.getSubscribedToIds().remove(author.getId());
        userRepo.save(author);
        userRepo.save(follower);
    }
    
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "profile-settings-data", key = "#userDetails.username"),
            @CacheEvict(value = "user-lite-info", key = "#userDetails.id"),
            @CacheEvict(value = "user-profile", key = "#userDetails.username")
    })
    public void updateUserByFields(UserDetailsImpl userDetails, Map<String, Object> fields, MultipartFile avatar, MultipartFile banner) {
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
            user.setAvatarUrl(fileService.saveFile(avatar));
        }
        if (banner != null) {
            user.setBannerUrl(fileService.saveFile(banner));
        }

        userRepo.save(user);
    }


    public Map<Integer, UserLiteDTO> getUsersLite(List<Integer> userIds) {
        return userRepo.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        UserEntity::getId,
                        UserLiteDTO::toModel
                ));
    }
}
