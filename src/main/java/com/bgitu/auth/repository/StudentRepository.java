package com.bgitu.auth.repository;

import com.bgitu.auth.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}