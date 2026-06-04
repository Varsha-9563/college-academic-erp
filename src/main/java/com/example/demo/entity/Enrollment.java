package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId;

    // many enrollments → one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // many enrollments → one course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // many enrollments → one faculty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(name = "attendance_percentage")
    private BigDecimal attendancePercentage;

    @Column(name = "internal_marks")
    private BigDecimal internalMarks;

    // constructors
    public Enrollment() {}

    public Enrollment(Student student, Course course, Faculty faculty) {
        this.student = student;
        this.course = course;
        this.faculty = faculty;
    }

    // getters & setters
    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public BigDecimal getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(BigDecimal attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public BigDecimal getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(BigDecimal internalMarks) {
        this.internalMarks = internalMarks;
    }
}
