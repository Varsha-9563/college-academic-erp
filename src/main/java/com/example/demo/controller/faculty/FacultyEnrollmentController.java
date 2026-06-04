package com.example.demo.controller.faculty;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.User;
import com.example.demo.entity.Faculty;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty/enrollments")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final FacultyRepository facultyRepository;

    public FacultyEnrollmentController(EnrollmentService enrollmentService,
                                       UserService userService,
                                       FacultyRepository facultyRepository) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.facultyRepository = facultyRepository;
    }

    @PutMapping("/attendance")
    public Enrollment updateAttendance(@RequestParam Integer studentId,
                                       @RequestParam Integer courseId,
                                       @RequestParam Double attendance,
                                       Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getByUsername(username);

        Faculty faculty = facultyRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        return enrollmentService.updateAttendance(
                faculty.getFacultyId(),
                studentId,
                courseId,
                attendance
        );
    }

    @PutMapping("/marks")
    public Enrollment updateMarks(@RequestParam Integer studentId,
                                  @RequestParam Integer courseId,
                                  @RequestParam Double marks,
                                  Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getByUsername(username);

        Faculty faculty = facultyRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        return enrollmentService.updateInternalMarks(
                faculty.getFacultyId(),
                studentId,
                courseId,
                marks
        );
    }
}
