package com.example.learning_jpa.controller;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.UserAuthenticationService;
import com.example.learning_jpa.service.result.AuthResult;
import com.example.learning_jpa.util.AccessJwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    @Value("${ACCESS_TOKEN_EXPIRATION_Time}")
    private int accessTokenExpiryTime;

    @Value("${REFRESH_TOKEN_EXPIATION_TIME}")
    private int cookieExpiryTime;

    @NonNull
    private ResponseEntity<GeneralResponseDto> getGeneralResponseDtoResponseEntity(AuthResult result) {
        GeneralResponseDto generalResponse = result.generalResponse();
        String accessToken = result.accessToken();
        String refreshToken = result.refreshToken();

        if (!generalResponse.isRes())
            return ResponseEntity.status(generalResponse.getStatusCode()).body(generalResponse);

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessTokenExpiryTime)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(cookieExpiryTime)
                .sameSite("Lax")
                .build();

        ResponseCookie userEmailCookie = ResponseCookie.from("USER_EMAIL", result.email())
                .httpOnly(false) // Allow JavaScript access if needed
                .secure(true)
                .path("/")
                .maxAge(cookieExpiryTime)
                .sameSite("Lax")
                .build();

        String roleValue = result.role() != null ? result.role().toString() : "";
        ResponseCookie userRoleCookie = ResponseCookie.from("USER_ROLE", roleValue)
                .httpOnly(false) // Allow JavaScript access if needed
                .secure(true)
                .path("/")
                .maxAge(cookieExpiryTime)
                .sameSite("Lax")
                .build();

        return ResponseEntity.status(generalResponse.getStatusCode())
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, userEmailCookie.toString())
                .header(HttpHeaders.SET_COOKIE, userRoleCookie.toString())
                .body(generalResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<GeneralResponseDto> signUp(@RequestBody UserSignUp userSignUp) {
        AuthResult result = userAuthenticationService.signUp(userSignUp);
        try {
            return getGeneralResponseDtoResponseEntity(result);
        } catch (Exception e) {
            GeneralResponseDto errorResponse = new GeneralResponseDto();
            errorResponse.setRes(false);
            errorResponse.setMsg("Invalid or expired refresh token.");
            errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponseDto> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        try {
            AuthResult result = userAuthenticationService.login(userLoginDto, request);
            return getGeneralResponseDtoResponseEntity(result);
        } catch (Exception e) {
            GeneralResponseDto errorResponse = new GeneralResponseDto();
            errorResponse.setRes(false);
            errorResponse.setMsg("Invalid or expired refresh token.");
            errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<GeneralResponseDto> refreshToken(HttpServletRequest request) {
        try {
            AuthResult result = userAuthenticationService.refreshToken(request);
            return getGeneralResponseDtoResponseEntity(result);
        } catch (Exception e) {
            GeneralResponseDto errorResponse = new GeneralResponseDto();
            errorResponse.setRes(false);
            errorResponse.setMsg("Invalid or expired refresh token.");
            errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<GeneralResponseDto> logout(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("ACCESS_TOKEN".equals(cookie.getName())) {
                        String jti = accessJwtUtil.extractJti(cookie.getValue());
                        userAuthenticationService.logout(jti);
                        break;
                    }
                }
            }

            ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                    .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();
            ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", "")
                    .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();
            ResponseCookie emailCookie = ResponseCookie.from("USER_EMAIL", "")
                    .httpOnly(false).secure(true).path("/").maxAge(0).sameSite("Lax").build();
            ResponseCookie roleCookie = ResponseCookie.from("USER_ROLE", "")
                    .httpOnly(false).secure(true).path("/").maxAge(0).sameSite("Lax").build();

            GeneralResponseDto response = new GeneralResponseDto();
            response.setRes(true);
            response.setMsg("Logged out successfully");
            response.setStatusCode(HttpServletResponse.SC_OK);

            return ResponseEntity.status(HttpServletResponse.SC_OK)
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, emailCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, roleCookie.toString())
                    .body(response);
        } catch (Exception e) {
            GeneralResponseDto errorResponse = new GeneralResponseDto();
            errorResponse.setRes(false);
            errorResponse.setMsg("Logout failed");
            errorResponse.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
        }
    }
}