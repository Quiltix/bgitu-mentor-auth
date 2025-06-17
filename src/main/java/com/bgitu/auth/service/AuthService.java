package com.bgitu.auth.service;

import com.bgitu.auth.dto.LoginRequestDTO;
import com.bgitu.auth.dto.RegisterRequestDto;
import com.bgitu.auth.model.Mentor;
import com.bgitu.auth.model.Role;
import com.bgitu.auth.model.Student;
import com.bgitu.auth.repository.MentorRepository;
import com.bgitu.auth.repository.StudentRepository;
import com.bgitu.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(RegisterRequestDto request) {
        // Проверка уникальности email
        if (mentorRepository.existsByEmail(request.getEmail()) ||
                studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        if (request.getRole() == Role.MENTOR) {
            Mentor mentor = new Mentor();
            mentor.setEmail(request.getEmail());
            mentor.setPassword(passwordEncoder.encode(request.getPassword()));
            mentor.setFirstName(request.getFirstName());
            mentor.setLastName(request.getLastName());
            mentorRepository.save(mentor);
        } else {
            Student student = new Student();
            student.setEmail(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            studentRepository.save(student);
        }

        return "User registered successfully";
    }

    public String authenticate(LoginRequestDTO request) {
        if (!mentorRepository.existsByEmail(request.getEmail()) ||
                !studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User not found");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtUtil.generateAccessToken(userDetails);
    }
}