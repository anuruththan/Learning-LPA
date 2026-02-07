package com.example.learning_jpa.service.result;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.enums.Roles;

public record AuthResult(
        GeneralResponseDto generalResponse,
        String email,
        Roles role,
        String accessToken,
        String refreshToken
) {
    // This must match the record component order exactly!
    public AuthResult(
            GeneralResponseDto generalResponse,
            String email,
            Roles role,
            String accessToken,
            String refreshToken
    ) {
        this.generalResponse = generalResponse;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}