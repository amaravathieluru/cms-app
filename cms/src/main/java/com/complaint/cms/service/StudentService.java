package com.complaint.cms.service;

import com.complaint.cms.model.Student;
import com.complaint.cms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new student
    public String registerStudent(Student student) {
        if (studentRepository.existsByUsername(student.getUsername())) {
            return "Username already taken. Please choose another.";
        }
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            return "Roll number already registered.";
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            return "Email already registered.";
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);
        return "success";
    }

    // Find student by username
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }

    // Find student by email
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email).orElse(null);
    }

    // Save student (used for password updates)
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
