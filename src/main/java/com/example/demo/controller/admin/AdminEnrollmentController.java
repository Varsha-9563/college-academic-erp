package com.example.demo.controller.admin;

import com.example.demo.dto.EnrollmentRequest;
import com.example.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/enrollments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public String enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        enrollmentService.enrollStudent(
                request.getStudentId(),
                request.getCourseId(),
                request.getFacultyId()
        );
        return "Student enrolled successfully";
    }
}
