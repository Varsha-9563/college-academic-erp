package com.example.demo.controller.faculty;

import com.example.demo.dto.CourseResponseDTO;
import com.example.demo.dto.EnrollmentResponseDTO;
import com.example.demo.entity.Faculty;
import com.example.demo.entity.User;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.service.CourseService;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PHASE 2 — Faculty self-service dashboard.
 *
 * All endpoints derive facultyId from JWT — same pattern as Phase 1's
 * FacultyEnrollmentController which you already have working.
 */
@RestController
@RequestMapping("/api/faculty")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyDashboardController {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final UserService userService;
    private final FacultyRepository facultyRepository;

    public FacultyDashboardController(EnrollmentService enrollmentService,
                                      CourseService courseService,
                                      UserService userService,
                                      FacultyRepository facultyRepository) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.userService = userService;
        this.facultyRepository = facultyRepository;
    }

    // ----------------------------------------------------------------
    // Helper: resolve Faculty from JWT — same approach as your existing
    // FacultyEnrollmentController (which already works)
    // ----------------------------------------------------------------
    private Faculty resolveFaculty(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getByUsername(username);
        return facultyRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Faculty profile not found for this user"));
    }

    // ----------------------------------------------------------------
    // GET /api/faculty/my-students
    // All students enrolled in courses taught by this faculty
    // ----------------------------------------------------------------
    @GetMapping("/my-students")
    public List<EnrollmentResponseDTO> getMyStudents(Authentication authentication) {
        Faculty faculty = resolveFaculty(authentication);
        return enrollmentService.getMyStudents(faculty.getFacultyId());
    }

    // ----------------------------------------------------------------
    // GET /api/faculty/my-courses
    // Distinct courses this faculty is assigned to (via course.assignedFaculty)
    // ----------------------------------------------------------------
    @GetMapping("/my-courses")
    public List<CourseResponseDTO> getMyCourses(Authentication authentication) {
        Faculty faculty = resolveFaculty(authentication);

        // Get all distinct courses where this faculty is the assigned faculty
        return courseService.getAllCourses()
                .stream()
                .filter(c -> c.getAssignedFaculty() != null
                        && c.getAssignedFaculty().getFacultyId().equals(faculty.getFacultyId()))
                .map(c -> new CourseResponseDTO(
                        c.getCourseId(),
                        c.getCourseCode(),
                        c.getCourseName(),
                        c.getDepartment().getDepartmentName()
                ))
                .collect(Collectors.toList());
    }
}
