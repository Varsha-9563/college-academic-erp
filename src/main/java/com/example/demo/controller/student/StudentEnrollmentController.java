package com.example.demo.controller.student;

import com.example.demo.entity.Enrollment;
import com.example.demo.service.EnrollmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/enrollments")
@PreAuthorize("hasRole('STUDENT')")
public class StudentEnrollmentController {

    private final EnrollmentService enrollmentService;

    public StudentEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable Integer studentId) {
        return enrollmentService.getStudentEnrollments(studentId);
    }
}
