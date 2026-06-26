package com.example.demo.service;

import com.example.demo.dto.EnrollmentResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             FacultyRepository facultyRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    // ---- Phase 1 methods (unchanged) ----

    public Enrollment enrollStudent(Integer studentId,
                                    Integer courseId,
                                    Integer facultyId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Integer studentDept = student.getDepartment().getDeptId();
        Integer courseDept  = course.getDepartment().getDeptId();
        Integer facultyDept = faculty.getDepartment().getDeptId();

        if (!studentDept.equals(courseDept))
            throw new RuntimeException("Student and Course belong to different departments");
        if (!facultyDept.equals(courseDept))
            throw new RuntimeException("Faculty and Course belong to different departments");
        if (!studentDept.equals(facultyDept))
            throw new RuntimeException("Faculty and Student belong to different departments");

        if (enrollmentRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId).isPresent())
            throw new RuntimeException("Student already enrolled in this course");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setFaculty(faculty);

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateAttendance(Integer facultyId, Integer studentId,
                                       Integer courseId, Double attendance) {

        Enrollment enrollment = enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!enrollment.getFaculty().getFacultyId().equals(facultyId))
            throw new RuntimeException("Faculty not authorized for this course");

        enrollment.setAttendancePercentage(BigDecimal.valueOf(attendance));
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateInternalMarks(Integer facultyId, Integer studentId,
                                          Integer courseId, Double marks) {

        Enrollment enrollment = enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!enrollment.getFaculty().getFacultyId().equals(facultyId))
            throw new RuntimeException("Faculty not authorized for this course");

        if (marks < 0 || marks > 100)
            throw new RuntimeException("Marks must be between 0 and 100");

        enrollment.setInternalMarks(BigDecimal.valueOf(marks));
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Integer studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId);
    }

    // ---- PHASE 2 additions ----

    /**
     * Student self-enrollment.
     *
     * The student only passes courseId. Their studentId comes from the JWT
     * (resolved by the controller before calling this method).
     * FacultyId is auto-resolved from course.assignedFaculty — no manual input needed.
     */
    public EnrollmentResponseDTO selfEnroll(Integer studentId, Integer courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Must have an assigned faculty before students can self-enroll
        if (course.getAssignedFaculty() == null) {
            throw new RuntimeException("No faculty assigned to this course yet. Cannot enroll.");
        }

        Faculty faculty = course.getAssignedFaculty();

        // Same department check
        if (!student.getDepartment().getDeptId().equals(course.getDepartment().getDeptId())) {
            throw new RuntimeException("You can only enroll in courses from your own department");
        }

        // Duplicate check
        if (enrollmentRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId).isPresent()) {
            throw new RuntimeException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setFaculty(faculty);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toDTO(saved);
    }

    /**
     * JWT-based: get all enrollments for the logged-in student.
     * Returns DTOs, not raw entities — no lazy-load issues.
     */
    public List<EnrollmentResponseDTO> getMyEnrollments(Integer studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * JWT-based: get all students enrolled in any course taught by this faculty.
     */
    public List<EnrollmentResponseDTO> getMyStudents(Integer facultyId) {
        return enrollmentRepository.findByFaculty_FacultyId(facultyId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a raw Enrollment entity → EnrollmentResponseDTO.
     * This is the single place where we access lazy fields, keeping
     * everything inside the service layer (within the transaction).
     */
    private EnrollmentResponseDTO toDTO(Enrollment e) {
        EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
        dto.setEnrollmentId(e.getEnrollmentId());

        dto.setCourseId(e.getCourse().getCourseId());
        dto.setCourseCode(e.getCourse().getCourseCode());
        dto.setCourseName(e.getCourse().getCourseName());

        dto.setFacultyId(e.getFaculty().getFacultyId());
        dto.setFacultyName(e.getFaculty().getName());

        dto.setStudentId(e.getStudent().getStudentId());
        dto.setStudentName(e.getStudent().getName());
        dto.setStudentUsn(e.getStudent().getUsn());

        dto.setAttendancePercentage(e.getAttendancePercentage());
        dto.setInternalMarks(e.getInternalMarks());

        return dto;
    }
}
