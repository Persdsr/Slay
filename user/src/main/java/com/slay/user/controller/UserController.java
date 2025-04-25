package com.slay.user.controller;

import com.slay.user.dto.response.course.TrainingCourseLiteDTO;
import com.slay.user.dto.response.user.UserLiteDTO;
import com.slay.user.dto.response.user.UserProfileDTO;
import com.slay.user.dto.response.user.UserProfileSettingDTO;
import com.slay.user.security.services.UserDetailsImpl;
import com.slay.user.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "User", description = "Пользователь")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить профиль автора курса",
            description = "Возвращает профиль автора курса по его имени пользователя. Профиль включает информацию об авторе, список его курсов, отзывы, а также информацию о подписке и покупке курсов текущим пользователем."
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getAuthorProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable("username") String username) {
        return new ResponseEntity<>(userService.getAuthorProfile(userDetails, username), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить минимальную информацию о пользователе по его id",
            description = "Возвращает минимальную информацию о пользователе по его id. Включает информацию об username и avatar"
    )
    @GetMapping("/lite")
    public ResponseEntity<UserLiteDTO> getUserLiteInfo(@RequestParam("userId") Integer userId) {
        return new ResponseEntity<>(userService.getUserLiteInfo(userId), HttpStatus.OK);
    }

    @GetMapping("/lite/batch")
    public Map<Integer, UserLiteDTO> getUsersLite(@RequestParam List<Integer> userIds) {
        return userService.getUsersLite(userIds);
    }

    @Operation(
            summary = "Получить данные профиля текущего пользователя",
            description = "Возвращает данные профиля текущего авторизованного пользователя. Эти данные включают имя пользователя, аватар, баннер, информацию о себе, имя и дату рождения."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileSettingDTO> getSettingsData(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userService.getProfileSettingsData(userDetails), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список купленных курсов пользователя",
            description = "Возвращает список курсов, купленных пользователем с указанным именем. Доступ к этому методу имеют только авторизованные пользователи, администраторы и модераторы, а также сам пользователь, чьи курсы запрашиваются."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/purchase-courses")
    public ResponseEntity<List<TrainingCourseLiteDTO>> getMyPurchaseCourses(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ResponseEntity<>(userService.getUserPurchaseCourses(userDetails), HttpStatus.OK);
    }

    @Operation(
            summary = "Подписаться на пользователя",
            description = "Позволяет текущему авторизованному пользователю подписаться на другого пользователя." +
        "После успешной подписки текущий пользователь добавляется в список подписчиков автора, а автор — в список подписок текущего пользователя."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/follow")
    public ResponseEntity<HttpStatus> handleFollowToUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam("author") String authorUsername) {
        userService.followToUser(userDetails, authorUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Отписаться от пользователя",
            description = "Позволяет текущему авторизованному пользователю отписаться от другого пользователя." +
        "После успешной отписки текущий пользователь удаляется из списка подписчиков автора, а автор — из списка подписок текущего пользователя."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unfollow")
    public ResponseEntity<HttpStatus> handleUnFollowToUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam("author") String author) {
        userService.unfollowUser(userDetails, author);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Обновить поля пользователя",
            description = "Позволяет обновить поля пользователя, включая аватар и баннер. Доступ к этому методу имеют только авторизованные пользователи, администраторы и модераторы, а также сам пользователь, чьи данные обновляются."
    )
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update_user_fields")
    public ResponseEntity<HttpStatus> updateUserFields(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart("body") Map<String, Object> fields,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart(value = "banner", required = false) MultipartFile banner) {
        userService.updateUserByFields(userDetails, fields, avatar, banner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
