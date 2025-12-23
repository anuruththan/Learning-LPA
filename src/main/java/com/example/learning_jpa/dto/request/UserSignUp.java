package com.example.learning_jpa.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUp {
    String firstName;
    String lastName;
    String MobileNumber;
    String email;
    String password;
    int role;
}
