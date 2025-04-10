package com.slay.course.service.user;

import com.slay.course.DTO.request.auth.SignInRequest;
import com.slay.course.DTO.request.auth.SignUpRequest;
import com.slay.course.entity.user.PasswordResetToken;
import com.slay.course.entity.user.UserEntity;
import com.slay.course.exception.EmailAlreadyExistsException;
import com.slay.course.exception.UsernameAlreadyExistsException;
import com.slay.course.repository.user.PasswordResetTokenRepository;
import com.slay.course.repository.user.UserRepo;
import com.slay.course.security.jwt.JwtUtils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Value("${slay.app.secret}")
    private String secretKey;

    @Value("${slay.app.lifetime}")
    private long tokenLifetime;

    private final PasswordResetTokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepo userRepo, PasswordEncoder encoder, JwtUtils jwtUtils, PasswordResetTokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Аутентифицирует пользователя на основе предоставленных учетных данных и возвращает JWT-токен.
     *
     * @param signInRequest объект {@link SignInRequest}, содержащий email и пароль пользователя.
     * @return JWT-токен.
     * @throws AuthenticationException если аутентификация не удалась (например, неверные учетные данные).
     */
    public String signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        return jwtUtils.generateJwtToken(authentication);
    }

    /**
     * Регистрирует нового пользователя на основе предоставленных данных.
     *
     * @param signUpRequest объект {@link SignUpRequest}, содержащий данные для регистрации:
     *                      имя пользователя, пароль и email.
     * @throws UsernameAlreadyExistsException если имя пользователя уже занято.
     * @throws EmailAlreadyExistsException если email уже зарегистрирован.
     */
    public void signUp(SignUpRequest signUpRequest) {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            throw UsernameAlreadyExistsException.builder().build();
        }

        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw EmailAlreadyExistsException.builder().build();
        }

        UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());

        userRepo.save(user);

    }

    public String refreshToken(String refreshToken) {
        try {
            if (isValidRefreshToken(refreshToken)) {
                String newAccessToken = generateNewAccessToken(refreshToken);
                return newAccessToken;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateNewAccessToken(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken).getBody();

        return Jwts.builder()
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifetime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Map<String, Object> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", userDetails.getUsername());
        userInfo.put("roles", roles);

        return userInfo;
    }

    public String createToken(UserEntity user) {
        Optional<PasswordResetToken> existingToken = Optional.ofNullable(tokenRepository.findByUser(user));
        if (existingToken.isPresent()) {
            PasswordResetToken resetToken = existingToken.get();
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000));
            tokenRepository.save(resetToken);
            return resetToken.getToken();
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000));
        tokenRepository.save(resetToken);
        return token;
    }

    public boolean isValidToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        return resetToken != null && resetToken.getExpiryDate().after(new Date());
    }

    public UserEntity getUserByToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        return resetToken != null ? resetToken.getUser() : null;
    }

    public void resetPassword(String token, String newPassword) {
        UserEntity user = getUserByToken(token);
        user.setPassword(encoder.encode(newPassword));
        userRepo.save(user);
    }
}
