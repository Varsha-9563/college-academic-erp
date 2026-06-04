package com.example.demo.controller.admin;

import com.example.demo.dto.FacultyRequest;
import com.example.demo.entity.Faculty;
import com.example.demo.service.FacultyService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faculty")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFacultyController {

    private final FacultyService facultyService;

    public AdminFacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@Valid @RequestBody FacultyRequest request) {
        return facultyService.createFaculty(
                request.getName(),
                request.getEmail(),
                request.getDeptId(),
                request.getUsername(),
                request.getPassword()
        );
    }

    @GetMapping
    public List<Faculty> getAllFaculty() {
        return facultyService.getAllFaculty();
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable Integer id) {
        facultyService.deleteFaculty(id);
    }
}
