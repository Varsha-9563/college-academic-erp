package com.example.demo.controller.admin;

import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDepartmentController {

    private final DepartmentService departmentService;

    public AdminDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public Department createDepartment( @Valid @RequestBody Department department) {
        return departmentService.createDepartment(department.getDepartmentName());
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
    }

    @PutMapping("/{deptId}/assign-hod/{facultyId}")
    public Department assignHod(@PathVariable Integer deptId,
                                @PathVariable Integer facultyId) {
        return departmentService.assignHod(deptId, facultyId);
    }
}
