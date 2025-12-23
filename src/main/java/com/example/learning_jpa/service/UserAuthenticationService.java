package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;

public interface UserAuthenticationService {

    public GeneralResponseDto signUp(UserSignUp userSignUp);

    public GeneralResponseDto login(UserLoginDto userLoginDto);

}
