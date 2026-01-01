package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.result.AuthResult;

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
     * @param refreshToken
     * **/
    AuthResult refreshToken(String accessToken,String refreshToken);
}
