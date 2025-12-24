package com.example.learning_jpa.dto.request;

import com.example.learning_jpa.enums.Roles;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUp {
    @NotBlank(message = "First Name is required")
    String firstName;
    String lastName;

    @NotBlank(message = "Mobile Number is required")
    String MobileNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;

    @NotBlank(message = "Role is required")
    Roles roles;
}
