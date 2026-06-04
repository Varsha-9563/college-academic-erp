package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.Faculty;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            FacultyRepository facultyRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // Create Department
    public Department createDepartment(String departmentName) {

        if (departmentRepository.existsByDepartmentName(departmentName)) {
            throw new RuntimeException("Department already exists");
        }

        Department department = new Department();
        department.setDepartmentName(departmentName);

        return departmentRepository.save(department);
    }

    // Get All Departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get Department By Id
    public Department getDepartmentById(Integer deptId) {
        return departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    // Delete Department
//    public void deleteDepartment(Integer deptId) {
//        if (!departmentRepository.existsById(deptId)) {
//            throw new RuntimeException("Department not found");
//        }
//        departmentRepository.deleteById(deptId);
//    }
    public void deleteDepartment(Integer deptId) {

        if (!departmentRepository.existsById(deptId)) {
            throw new RuntimeException("Department not found");
        }

        if (!studentRepository.findByDepartment_DeptId(deptId).isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete department with students"
            );
        }

        if (!facultyRepository.findByDepartment_DeptId(deptId).isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete department with faculty"
            );
        }

        if (!courseRepository.findByDepartment_DeptId(deptId).isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete department with courses"
            );
        }

        departmentRepository.deleteById(deptId);
    }

    // Assign HOD
    public Department assignHod(Integer deptId, Integer facultyId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // Business Rule: Faculty must belong to same department
        if (!faculty.getDepartment().getDeptId().equals(deptId)) {
            throw new RuntimeException("Faculty does not belong to this department");
        }

        department.setHod(faculty);

        return departmentRepository.save(department);
    }
}
