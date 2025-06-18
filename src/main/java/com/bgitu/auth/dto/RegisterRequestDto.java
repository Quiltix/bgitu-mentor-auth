package com.bgitu.auth.dto;

import com.bgitu.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;


@Value
public class RegisterRequestDto {
    @Email
    @NotBlank
    String email;

    @Size(min = 5, message = "password must be at least 5 characters long")
    @NotBlank
    String password;

    String firstName;

    String lastName;
    @Schema(
            description = "User role",
            example = "STUDENT",
            allowableValues = {"STUDENT", "MENTOR"},
            implementation = Role.class
    )
    Role role;




}