package com.bgitu.auth.service;

import com.bgitu.auth.dto.JwtAuthenticationResponseDTO;
import com.bgitu.auth.dto.LoginRequestDTO;
import com.bgitu.auth.dto.RegisterRequestDto;
import com.bgitu.auth.model.Mentor;
import com.bgitu.auth.model.Role;
import com.bgitu.auth.model.Student;
import com.bgitu.auth.repository.MentorRepository;
import com.bgitu.auth.repository.StudentRepository;
import com.bgitu.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public JwtAuthenticationResponseDTO register(RegisterRequestDto request) {
        if (mentorRepository.existsByEmail(request.getEmail()) ||
                studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserDetails userDetails;
        if (request.getRole() == Role.MENTOR) {
            Mentor mentor = createMentor(request);
            mentorRepository.save(mentor);
            userDetails = createUserDetails(mentor, Role.MENTOR);
        } else {
            Student student = createStudent(request);
            studentRepository.save(student);
            userDetails = createUserDetails(student, Role.STUDENT);
        }

        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return new JwtAuthenticationResponseDTO(accessToken);
    }

    private Mentor createMentor(RegisterRequestDto request) {
        Mentor mentor = new Mentor();
        mentor.setEmail(request.getEmail());
        mentor.setPassword(passwordEncoder.encode(request.getPassword()));
        mentor.setFirstName(request.getFirstName());
        mentor.setLastName(request.getLastName());
        return mentor;
    }

    private Student createStudent(RegisterRequestDto request) {
        Student student = new Student();
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        return student;
    }

    private UserDetails createUserDetails(Object user, Role role) {
        String email;
        String password;

        if (user instanceof Mentor mentor) {
            email = mentor.getEmail();
            password = mentor.getPassword();
        } else {
            Student student = (Student) user;
            email = student.getEmail();
            password = student.getPassword();
        }

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
        );
    }
    public JwtAuthenticationResponseDTO authenticate(LoginRequestDTO request) {
        // Аутентификация через Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Генерация токенов
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return new JwtAuthenticationResponseDTO(accessToken);
    }
}
