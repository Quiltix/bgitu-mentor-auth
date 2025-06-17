package com.bgitu.auth.repository;

import com.bgitu.auth.model.Mentor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    Optional<Mentor> findByEmail(String email);

    boolean existsByEmail(@Email @NotBlank String email);
}