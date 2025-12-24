package com.example.learning_jpa.service.result;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.enums.Roles;

public record AuthResult(
        GeneralResponseDto generalResponse,
        String email,
        Roles role,
        String accessToken,
        String refreshToken) {
}
