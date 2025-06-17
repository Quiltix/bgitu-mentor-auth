package com.bgitu.auth.service;

import com.bgitu.auth.dto.LoginRequestDTO;
import com.bgitu.auth.dto.MessageDTO;
import com.bgitu.auth.repository.MentorRepository;
import com.bgitu.auth.repository.StudentRepository;
import com.bgitu.auth.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthService {

    private StudentRepository studentRepository;
    private MentorRepository menthorRepository;

    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthService(StudentRepository studentRepository, MentorRepository menthorRepository, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.menthorRepository = menthorRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<MessageDTO> authenticate(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken);
    }
}
