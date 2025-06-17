package com.bgitu.auth.service;

import com.bgitu.auth.model.Mentor;
import com.bgitu.auth.model.Role;
import com.bgitu.auth.repository.MentorRepository;
import com.bgitu.auth.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Проверяем в обеих таблицах
        Optional<Mentor> mentor = mentorRepository.findByEmail(email);
        if (mentor.isPresent()) {
            return createUserDetails(mentor.get(), Role.MENTOR);
        }

        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return createUserDetails(student.get(), Role.STUDENT);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails createUserDetails(Object user, Role role) {
        String email;
        String password;

        if (user instanceof Mentor) {
            Mentor mentor = (Mentor) user;
            email = mentor.getEmail();
            password = mentor.getPassword();
        } else if (user instanceof Student) {
            Student student = (Student) user;
            email = student.getEmail();
            password = student.getPassword();
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
        );
    }
}