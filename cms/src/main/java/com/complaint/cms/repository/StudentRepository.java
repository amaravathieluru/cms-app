package com.complaint.cms.repository;

import com.complaint.cms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByRollNumber(String rollNumber);
    boolean existsByEmail(String email);
}
