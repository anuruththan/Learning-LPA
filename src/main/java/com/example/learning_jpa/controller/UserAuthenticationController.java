package com.example.learning_jpa.controller;


import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.service.UserAuthenticationService;
import com.example.learning_jpa.util.AccessJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    @Value("${COOKIE_EXPIRATION_TIME}")
    private int cookieExpiryTime;

    @PostMapping("/login")
    public ResponseEntity<GeneralResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        GeneralResponseDto generalResponse = userAuthenticationService.login(userLoginDto);

        if (!generalResponse.isRes())
            return ResponseEntity.status(generalResponse.getStatusCode()).body(generalResponse);

        String token = accessJwtUtil.generateToken(userLoginDto.getEmail(), 1);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(cookieExpiryTime)
                .sameSite("Lax") // or "Strict"
                .build();

        return ResponseEntity.status(generalResponse.getStatusCode())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(generalResponse);
    }

}
