package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByUsn(String usn);

    Optional<Student> findByEmail(String email);

    List<Student> findByDepartment_DeptId(Integer deptId);

    boolean existsByUsn(String usn);

    boolean existsByEmail(String email);

    // ✅ PHASE 2 — needed for JWT-based self-service APIs
    // Maps the logged-in user's userId to their Student record
    Optional<Student> findByUser_UserId(Integer userId);
}
