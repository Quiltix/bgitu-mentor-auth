package com.bgitu.auth.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class LoginRequestDTO {
    @Email
    @NotBlank
    String email;

    @Size(min = 5, message = "password must be at least 5 characters long")
    @NotBlank
    String password;

}
