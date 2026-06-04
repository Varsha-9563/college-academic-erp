package com.example.demo.repository;

import com.example.demo.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    Optional<Faculty> findByEmail(String email);
    Optional<Faculty> findByUser_UserId(Integer userId);
    List<Faculty> findByDepartment_DeptId(Integer deptId);

    boolean existsByEmail(String email);
}
