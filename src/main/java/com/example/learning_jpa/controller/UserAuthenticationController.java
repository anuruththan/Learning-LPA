package com.example.learning_jpa.controller;


import com.example.learning_jpa.dto.GeneralResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {

    @GetMapping("/login")
    public ResponseEntity<GeneralResponseDto> login(){
        GeneralResponseDto generalResponse = new GeneralResponseDto();
        generalResponse.setRes(true);
        generalResponse.setMsg("Login Successful");
        generalResponse.setStatusCode(200);
        return new ResponseEntity<>(generalResponse, HttpStatus.valueOf(generalResponse.getStatusCode()));
    }

}
