package com.bgitu.auth.dto;

import com.bgitu.auth.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
public class RegisterRequestDto {
    @Email
    @NotBlank
    String email;

    @Min(value = 5, message = "password must be at least 5 characters long")
    @NotBlank
    String password;

    String firstName;

    String lastName;

    Role role;




}