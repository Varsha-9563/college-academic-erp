package com.example.demo.service;

import com.example.demo.dto.CourseResponseDTO;
import com.example.demo.entity.Course;
import com.example.demo.entity.Department;
import com.example.demo.entity.Faculty;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FacultyRepository facultyRepository;  // ✅ PHASE 2 addition

    public CourseService(CourseRepository courseRepository,
                         DepartmentRepository departmentRepository,
                         EnrollmentRepository enrollmentRepository,
                         FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.facultyRepository = facultyRepository;
    }

    // Unchanged from Phase 1
    public Course createCourse(String code, String name, Integer deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Course course = new Course();
        course.setCourseCode(code);
        course.setCourseName(name);
        course.setDepartment(department);

        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getCoursesByDepartment(Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new RuntimeException("Department not found");
        }
        return courseRepository.findByDepartment_DeptId(deptId);
    }

    public void deleteCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByCourse_CourseId(courseId)) {
            throw new RuntimeException("Cannot delete course with enrolled students");
        }

        courseRepository.delete(course);
    }

    // ✅ PHASE 2 — Admin assigns a faculty to a course
    // This is what makes student self-enrollment possible:
    // when a student picks a courseId, we read course.assignedFaculty
    public Course assignFacultyToCourse(Integer courseId, Integer facultyId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // Business rule: faculty must belong to the same dept as the course
        if (!faculty.getDepartment().getDeptId().equals(course.getDepartment().getDeptId())) {
            throw new RuntimeException("Faculty does not belong to this course's department");
        }

        course.setAssignedFaculty(faculty);
        return courseRepository.save(course);
    }

    // ✅ PHASE 2 — Returns courses that have a faculty assigned
    // A student can only self-enroll in courses where a faculty is set
    public List<CourseResponseDTO> getAvailableCourses() {
        return courseRepository.findAll()
                .stream()
                .filter(c -> c.getAssignedFaculty() != null)
                .map(c -> new CourseResponseDTO(
                        c.getCourseId(),
                        c.getCourseCode(),
                        c.getCourseName(),
                        c.getDepartment().getDepartmentName()
                ))
                .collect(Collectors.toList());
    }

    // ✅ PHASE 2 — Available courses filtered by the student's own department
    public List<CourseResponseDTO> getAvailableCoursesByDept(Integer deptId) {
        return courseRepository.findByDepartment_DeptId(deptId)
                .stream()
                .filter(c -> c.getAssignedFaculty() != null)
                .map(c -> new CourseResponseDTO(
                        c.getCourseId(),
                        c.getCourseCode(),
                        c.getCourseName(),
                        c.getDepartment().getDepartmentName()
                ))
                .collect(Collectors.toList());
    }
}
