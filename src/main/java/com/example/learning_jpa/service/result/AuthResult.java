package com.example.learning_jpa.service.result;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthResult(
        GeneralResponseDto generalResponse,
        String email,
        Roles role,
        String accessToken,
        String refreshToken) {
    public AuthResult(GeneralResponseDto response, String accessToken, String refreshToken, @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String email, Roles roles){};
}
