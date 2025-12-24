package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.result.AuthResult;
import jakarta.servlet.http.HttpServletResponse;

public interface UserAuthenticationService {

    /**
     * @param userSignUp
     * **/
    AuthResult signUp(UserSignUp userSignUp);

    /**
     * @param userLoginDto
     * **/
    AuthResult login(UserLoginDto userLoginDto);

    /**
     * @param response
     * **/
    void logout(HttpServletResponse response);

    /**
     * @param refreshToken
     * **/
    AuthResult refreshToken(String accessToken,String refreshToken);
}
