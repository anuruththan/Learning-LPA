package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.entity.User;
import com.example.learning_jpa.entity.UserSession;
import com.example.learning_jpa.enums.Roles;
import com.example.learning_jpa.repository.UserAuthRepository;
import com.example.learning_jpa.service.result.AuthResult;
import com.example.learning_jpa.util.AccessJwtUtil;
import com.example.learning_jpa.util.HashUtil;
import com.example.learning_jpa.util.RefreshJwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserSessionService sessionService;

    @Autowired
    private HashUtil passwordEncoder;

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    @Autowired
    private RefreshJwtUtil refreshJwtUtil;

    public AuthResult signUp(UserSignUp userSignUp) {
        GeneralResponseDto response = new GeneralResponseDto();

        // Validate role
        if (!Roles.isValidRole(userSignUp.getRoles())) {
            response.setRes(false);
            response.setMsg("Invalid role provided");
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new AuthResult(response, (String) null,  null, null, null);
        }

        // Check if email already exists
        if (userAuthRepository.existsByEmail(userSignUp.getEmail())) {
            response.setRes(false);
            response.setMsg("Email already exists");
            response.setStatusCode(HttpStatus.CONFLICT.value());
            return new AuthResult(response, (String) null,  null, null, null);
        }

        // Create user
        User user = new User();
        user.setFirstName(userSignUp.getFirstName());
        user.setLastName(userSignUp.getLastName());
        user.setMobileNumber(userSignUp.getMobileNumber());
        user.setEmail(userSignUp.getEmail());
        user.setPassword(passwordEncoder.hashPwd(userSignUp.getPassword()));
        user.setRoles(userSignUp.getRoles());

        userAuthRepository.save(user);

        response.setRes(true);
        response.setMsg("User registered successfully");
        response.setStatusCode(HttpStatus.CREATED.value());

        return new AuthResult(response,  null,  null, null, null);
    }

    public AuthResult login(UserLoginDto userLoginDto, HttpServletRequest request) {
        GeneralResponseDto response = new GeneralResponseDto();

        User user = userAuthRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!Objects.equals(user.getPassword(), passwordEncoder.hashPwd(userLoginDto.getPassword()))) {
            response.setRes(false);
            response.setMsg("Invalid email or password");
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return new AuthResult(response,  null,  null, null, null);
        }

        // Check if employee already has active session (single device restriction)
        if (sessionService.hasActiveSession(user)) {
            response.setRes(false);
            response.setMsg("You are already logged in on another device. Please logout from that device first.");
            response.setStatusCode(HttpStatus.FORBIDDEN.value());
            return new AuthResult(response,  null,  null, null, null);
        }

        // Create new session
        UserSession session = sessionService.createSession(user, request);
        String accessToken = accessJwtUtil.generateToken(user.getEmail(), user.getRoles(), session.getJti());

        response.setRes(true);
        response.setMsg("Login successful");
        response.setStatusCode(HttpStatus.OK.value());

        return new AuthResult(response, user.getEmail(), user.getRoles(), accessToken, session.getRefreshToken());
    }

    public AuthResult refreshToken(HttpServletRequest request) {
        GeneralResponseDto response = new GeneralResponseDto();

        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not found");
        }

        String email = refreshJwtUtil.extractEmail(refreshToken);
        User user = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Refresh session with new tokens
        UserSession newSession = sessionService.refreshSession(refreshToken, user, request);
        String newAccessToken = accessJwtUtil.generateToken(user.getEmail(), user.getRoles(), newSession.getJti());

        response.setRes(true);
        response.setMsg("Token refreshed successfully");
        response.setStatusCode(HttpStatus.OK.value());

        return new AuthResult(response , user.getEmail(), user.getRoles(), newAccessToken, newSession.getRefreshToken());
    }

    public void logout(String jti) {
        sessionService.logoutSession(jti);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("REFRESH_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}