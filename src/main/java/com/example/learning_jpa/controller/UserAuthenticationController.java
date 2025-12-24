package com.example.learning_jpa.controller;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.UserAuthenticationService;
import com.example.learning_jpa.service.result.AuthResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;


@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Value("${REFRESH_TOKEN_EXPIATION_TIME}")
    private int cookieExpiryTime;

    public GeneralResponseDto generalResponse;


    @NonNull
    private ResponseEntity<GeneralResponseDto> getGeneralResponseDtoResponseEntity(AuthResult result) {

        generalResponse = result.generalResponse();
        String accessToken = result.accessToken();
        String refreshToken = result.refreshToken();

        if (!generalResponse.isRes())
            return ResponseEntity.status(generalResponse.getStatusCode()).body(generalResponse);

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(cookieExpiryTime)
                .sameSite("Lax") // or "Strict"
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(cookieExpiryTime)
                .sameSite("Lax") // or "Strict"
                .build();

        ResponseCookie userEmailCookie = ResponseCookie.from("USER_EMAIL", result.email())
                .httpOnly(true)
                .secure(true)
                .maxAge(cookieExpiryTime)
                .sameSite("Lax") // or "Strict"
                .build();

        ResponseCookie userRoleCookie = ResponseCookie.from("USER_ROLE", result.role().toString())
                .httpOnly(true)
                .secure(true)
                .maxAge(cookieExpiryTime)
                .sameSite("Lax") // or "Strict"
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
        return getGeneralResponseDtoResponseEntity(result);
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        AuthResult result = userAuthenticationService.login(userLoginDto);
        return getGeneralResponseDtoResponseEntity(result);
    }

    @GetMapping("/refresh")
    public ResponseEntity<GeneralResponseDto> refreshToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) throw new IllegalArgumentException("Missing refresh token.");

            String accessToken = null;
            String refreshToken = null;

            for (Cookie cookie : cookies) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }

            for (Cookie cookie : cookies) {
                if ("REFRESH_TOKEN".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }

            if (refreshToken == null) throw new IllegalArgumentException("Missing refresh token.");

            AuthResult result = userAuthenticationService.refreshToken(accessToken, refreshToken);

            if (!result.generalResponse().isRes()) {
                return ResponseEntity.status(result.generalResponse().getStatusCode()).body(result.generalResponse());
            }

            return getGeneralResponseDtoResponseEntity(result); // Use the existing method to set cookies
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
        GeneralResponseDto generalResponseDto = new GeneralResponseDto();
        generalResponseDto.setData(null);
        generalResponseDto.setRes(true);
        generalResponseDto.setMsg("Cookies has been cleared.");
        generalResponseDto.setStatusCode(HttpServletResponse.SC_OK);
        AuthResult authResult = new AuthResult(generalResponse, null, null, null, null);
        return getGeneralResponseDtoResponseEntity(authResult);
    }

}
