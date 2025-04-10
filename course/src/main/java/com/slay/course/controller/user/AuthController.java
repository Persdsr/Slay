package com.slay.course.controller.user;

import com.slay.course.DTO.request.auth.SignInRequest;
import com.slay.course.DTO.request.auth.SignUpRequest;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.EmailNotFoundException;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.service.mail.EmailService;
import com.slay.course.service.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
@AllArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepo userRepository;
    private final EmailService emailService;

    @Operation(
            summary = "Авторизация пользователя",
            description = "Аутентифицирует пользователя по email и паролю. В случае успешной аутентификации возвращает JWT токен, который можно использовать для доступа к защищенным эндпоинтам."
    )
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody SignInRequest loginRequest) {
        return new ResponseEntity<>(authService.signIn(loginRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя в системе. При успешной регистрации возвращает ответ с сообщением о успешном завершении операции."
    )
    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить access token",
            description = "Обновляет access token с использованием refresh token. Если refresh token валиден, возвращается новый access token. В противном случае возвращается статус `401 Unauthorized`."
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);

        if (newAccessToken != null) {
            return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(
            summary = "Получить информацию о текущем пользователе",
            description = "Возвращает информацию о текущем авторизованном пользователе, включая его имя и роли."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(authService.getUserInfo(userDetails), HttpStatus.OK);
    }

    @Operation(
            summary = "Запрос на сброс пароля",
            description = "Отправляет письмо с инструкцией для сброса пароля на указанный email. Если пользователь с таким email не найден, возвращает ошибку."
    )
    @PostMapping("/forgot")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestParam String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> EmailNotFoundException.builder().build()
        );

        String token = authService.createToken(user);
        emailService.sendPasswordResetEmail(email, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Сбросить пароль",
            description = "Сбрасывает пароль пользователя на новый, если токен сброса пароля валиден. Если токен невалиден, возвращает ошибку."
    )
    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!authService.isValidToken(token)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        authService.resetPassword(token, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
