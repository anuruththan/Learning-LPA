package com.example.learning_jpa.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    String email;
    @NotBlank(message = "Password is required.")
    String password;
}
