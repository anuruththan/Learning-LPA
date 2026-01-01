package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.result.AuthResult;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAuthenticationService {

    /**
     *  Signs up user; returns auth result
     * @param userSignUp
     * **/
    AuthResult signUp(UserSignUp userSignUp);

    /**
     * @param userLoginDto
     * **/
    AuthResult login(UserLoginDto userLoginDto);

    /**
     * @param request
     * **/
    AuthResult refreshToken( HttpServletRequest request);
}
