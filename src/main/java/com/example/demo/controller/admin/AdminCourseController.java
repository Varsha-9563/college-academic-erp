package com.example.demo.controller.admin;

import com.example.demo.dto.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course createCourse( @Valid @RequestBody CourseRequest request) {
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
}
