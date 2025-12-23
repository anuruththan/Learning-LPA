package com.example.learning_jpa.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {
    String email;
    String password;
}
