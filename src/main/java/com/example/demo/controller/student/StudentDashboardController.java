package com.example.demo.controller.student;

import com.example.demo.dto.CourseResponseDTO;
import com.example.demo.dto.EnrollmentResponseDTO;
import com.example.demo.dto.SelfEnrollRequest;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PHASE 2 — Student self-service dashboard.
 *
 * ALL endpoints extract studentId from JWT — no studentId in any URL.
 * This prevents a student from viewing another student's data by changing the path param.
 *
 * Pattern:
 *   1. authentication.getName()       → username (from JWT subject)
 *   2. userService.getByUsername()    → User entity
 *   3. studentRepository.findByUser_UserId() → Student entity
 *   4. use student.getStudentId() for service calls
 */
@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentDashboardController {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final UserService userService;
    private final StudentRepository studentRepository;

    public StudentDashboardController(EnrollmentService enrollmentService,
                                      CourseService courseService,
                                      UserService userService,
                                      StudentRepository studentRepository) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.userService = userService;
        this.studentRepository = studentRepository;
    }

    // ----------------------------------------------------------------
    // Helper: resolve Student from JWT Authentication object
    // Reused by every endpoint in this controller
    // ----------------------------------------------------------------
    private Student resolveStudent(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getByUsername(username);
        return studentRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Student profile not found for this user"));
    }

    // ----------------------------------------------------------------
    // GET /api/student/my-enrollments
    // Returns all courses the logged-in student is enrolled in
    // ----------------------------------------------------------------
    @GetMapping("/my-enrollments")
    public List<EnrollmentResponseDTO> getMyEnrollments(Authentication authentication) {
        Student student = resolveStudent(authentication);
        return enrollmentService.getMyEnrollments(student.getStudentId());
    }

    // ----------------------------------------------------------------
    // GET /api/student/my-courses
    // Convenience alias — same data, different name for clarity
    // ----------------------------------------------------------------
    @GetMapping("/my-courses")
    public List<EnrollmentResponseDTO> getMyCourses(Authentication authentication) {
        Student student = resolveStudent(authentication);
        return enrollmentService.getMyEnrollments(student.getStudentId());
    }

    // ----------------------------------------------------------------
    // GET /api/student/available-courses
    // Shows courses the student CAN enroll in (same dept, has faculty, not already enrolled)
    // ----------------------------------------------------------------
    @GetMapping("/available-courses")
    public List<CourseResponseDTO> getAvailableCourses(Authentication authentication) {
        Student student = resolveStudent(authentication);
        // Returns only courses from student's dept that have a faculty assigned
        return courseService.getAvailableCoursesByDept(student.getDepartment().getDeptId());
    }

    // ----------------------------------------------------------------
    // POST /api/student/enroll
    // Student self-enrolls by picking a courseId
    // facultyId is auto-resolved from course.assignedFaculty
    //
    // Request body: { "courseId": 3 }
    // ----------------------------------------------------------------
    @PostMapping("/enroll")
    public EnrollmentResponseDTO selfEnroll(@RequestBody SelfEnrollRequest request,
                                            Authentication authentication) {
        Student student = resolveStudent(authentication);
        return enrollmentService.selfEnroll(student.getStudentId(), request.getCourseId());
    }
}
