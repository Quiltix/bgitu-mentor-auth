package com.bgitu.auth.service;

import com.bgitu.auth.model.Mentor;
import com.bgitu.auth.model.Role;
import com.bgitu.auth.model.Student;
import com.bgitu.auth.repository.MentorRepository;
import com.bgitu.auth.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ищем сначала в менторах
        Mentor mentor = mentorRepository.findByEmail(email)
                .orElse(null);

        if (mentor != null) {
            return createUserDetails(mentor.getEmail(), mentor.getPassword(), Role.MENTOR);
        }

        // Ищем в студентах
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return createUserDetails(student.getEmail(), student.getPassword(), Role.STUDENT);
    }

    private UserDetails createUserDetails(String email, String password, Role role) {
        // Гарантируем, что будет только одна роль
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(role.name())
        );

        return new User(email, password, authorities);
    }
}