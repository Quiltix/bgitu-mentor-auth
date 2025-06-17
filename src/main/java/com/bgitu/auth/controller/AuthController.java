package com.bgitu.auth.controller;


import com.bgitu.auth.dto.JwtAuthenticationResponseDTO;
import com.bgitu.auth.dto.LoginRequestDTO;
import com.bgitu.auth.dto.MessageDTO;
import com.bgitu.auth.dto.RegisterRequestDto;
import com.bgitu.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageDTO> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok( new MessageDTO(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(new JwtAuthenticationResponseDTO(authService.authenticate(request)));
    }
}
