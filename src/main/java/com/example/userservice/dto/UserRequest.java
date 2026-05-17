package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Incoming JSON body for registration.
 */
public record UserRequest(

        @NotBlank(message = "username is required")
        @Size(min = 3, max = 30, message = "username must be between 3 and 30 characters")
        String username,

        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 8, message = "password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "password must contain at least one uppercase letter and one digit")
        String password
) {}