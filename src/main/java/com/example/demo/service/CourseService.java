package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Department;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository,
                         DepartmentRepository departmentRepository,
                         EnrollmentRepository enrollmentRepository) {

        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // 1️⃣ Create Course
    public Course createCourse(String code, String name, Integer deptId) {

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Course course = new Course();
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setDepartment(department);

        return courseRepository.save(course);
    }

    // 2️⃣ Get All Courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 3️⃣ Get Course By Id
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // 4️⃣ Get Courses By Department
    public List<Course> getCoursesByDepartment(Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new RuntimeException("Department not found");
        }

        return courseRepository.findByDepartment_DeptId(deptId);
    }
    public void deleteCourse(Integer courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean hasEnrollments =
                enrollmentRepository.existsByCourse_CourseId(courseId);

        if (hasEnrollments) {
            throw new RuntimeException("Cannot delete course with enrolled students");
        }

        courseRepository.delete(course);
    }
}
