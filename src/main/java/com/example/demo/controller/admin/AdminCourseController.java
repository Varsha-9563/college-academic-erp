package com.example.demo.controller.admin;

import com.example.demo.dto.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PHASE 2 — Added PUT /{courseId}/assign-faculty/{facultyId}
 *
 * Admin assigns which faculty teaches a given course.
 * This is the prerequisite for student self-enrollment to work.
 *
 * Flow:
 *   Admin: PUT /api/admin/courses/3/assign-faculty/7
 *   → course 3 now has faculty 7 set as assignedFaculty
 *   → students can now self-enroll in course 3
 *   → system auto-fills facultyId = 7 in their enrollment record
 */
@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Phase 1 endpoints — unchanged
    @PostMapping
    public Course createCourse(@RequestBody CourseRequest request) {
        return courseService.createCourse(
                request.getCourseCode(),
                request.getCourseName(),
                request.getDeptId()
        );
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
    }

    // ✅ PHASE 2 — Assign a faculty to a course
    @PutMapping("/{courseId}/assign-faculty/{facultyId}")
    public Course assignFaculty(@PathVariable Integer courseId,
                                @PathVariable Integer facultyId) {
        return courseService.assignFacultyToCourse(courseId, facultyId);
    }
}
