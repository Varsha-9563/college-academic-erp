package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    // 1️⃣ Enroll Student
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
        Integer courseDept = course.getDepartment().getDeptId();
        Integer facultyDept = faculty.getDepartment().getDeptId();

        // Rule 1
        if (!studentDept.equals(courseDept)) {
            throw new RuntimeException("Student and Course belong to different departments");
        }

        // Rule 2
        if (!facultyDept.equals(courseDept)) {
            throw new RuntimeException("Faculty and Course belong to different departments");
        }

        // Rule 3
        if (!studentDept.equals(facultyDept)) {
            throw new RuntimeException("Faculty and Student belong to different departments");
        }

        // Rule 4 - duplicate prevention
        if (enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setFaculty(faculty);

        return enrollmentRepository.save(enrollment);
    }

    // 2️⃣ Update Attendance
    public Enrollment updateAttendance(Integer facultyId,
                                       Integer studentId,
                                       Integer courseId,
                                       Double attendance) {

        Enrollment enrollment = enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // 🔒 Authorization Check
        if (!enrollment.getFaculty().getFacultyId().equals(facultyId)) {
            throw new RuntimeException("Faculty not authorized for this course");
        }

        enrollment.setAttendancePercentage(BigDecimal.valueOf(attendance));

        return enrollmentRepository.save(enrollment);
    }


    // 3️⃣ Update Internal Marks
    public Enrollment updateInternalMarks(Integer facultyId,
                                          Integer studentId,
                                          Integer courseId,
                                          Double marks) {

        Enrollment enrollment = enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // 🔒 Authorization Check
        if (!enrollment.getFaculty().getFacultyId().equals(facultyId)) {
            throw new RuntimeException("Faculty not authorized for this course");
        }

        // Optional validation
        if (marks < 0 || marks > 100) {
            throw new RuntimeException("Marks must be between 0 and 100");
        }

        enrollment.setInternalMarks(BigDecimal.valueOf(marks));

        return enrollmentRepository.save(enrollment);
    }


    // 4️⃣ Get Student Enrollments
    public List<Enrollment> getStudentEnrollments(Integer studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId);
    }
}
