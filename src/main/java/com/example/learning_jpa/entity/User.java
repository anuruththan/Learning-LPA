package com.example.learning_jpa.entity;

import com.example.learning_jpa.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;



@Entity
@Table(name = "users")
@Getter
@Setter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 50, message = "Name must be between 3 to 50 characters")
    private String firstName;

    @Size(min = 3, max = 50, message = "Name must be between 3 to 50 characters")
    private String lastName;

    @NotBlank(message = "Mobile number cannot be empty")
    @Size(min = 9, max = 14, message = "Mobile number must be between 9 to 14 characters")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles roles;
}
