package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.Faculty;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyService(FacultyRepository facultyRepository,
                          DepartmentRepository departmentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create Faculty
    public Faculty createFaculty(String name,
                                 String email,
                                 Integer deptId,
                                 String username,
                                 String rawPassword) {

        if (facultyRepository.existsByEmail(email)) {
            throw new RuntimeException("Faculty email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Create User first
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(Role.FACULTY);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create Faculty
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setEmail(email);
        faculty.setDepartment(department);
        faculty.setUser(savedUser);

        return facultyRepository.save(faculty);
    }

    // Get Faculty by ID
    public Faculty getFacultyById(Integer facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    // Get All Faculty
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    // Get Faculty by Department
    public List<Faculty> getFacultyByDepartment(Integer deptId) {
        return facultyRepository.findByDepartment_DeptId(deptId);
    }

    // Delete Faculty
    public void deleteFaculty(Integer facultyId) {
        facultyRepository.deleteById(facultyId);
    }
}
