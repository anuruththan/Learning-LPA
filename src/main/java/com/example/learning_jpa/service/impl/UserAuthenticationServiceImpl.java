package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.UserLoginDto;
import com.example.learning_jpa.dto.request.UserSignUp;
import com.example.learning_jpa.service.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    public GeneralResponseDto generalResponse;

    @Override
    public GeneralResponseDto signUp(UserSignUp userSignUp) {
        return new GeneralResponseDto();
    }

    @Override
    public GeneralResponseDto login(UserLoginDto userLoginDto) {
        generalResponse = new GeneralResponseDto();
        generalResponse.setData(userLoginDto);
        generalResponse.setRes(true);
        generalResponse.setMsg("Login Successful");
        generalResponse.setStatusCode(200);
        return generalResponse;
    }

}
