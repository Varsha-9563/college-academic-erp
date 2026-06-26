package com.example.demo.dto;
import java.math.BigDecimal;

/**
 * PHASE 2 — Response DTO for enrollment data.
 *
 * Why this exists:
 * Phase 1 returned raw Enrollment entity which has lazy-loaded Student, Course,
 * Faculty inside it — causing either LazyInitializationException or N+1 queries,
 * and leaking internal entity structure to the client.
 *
 * This DTO lets the service layer control exactly what goes out.
 */

public class EnrollmentResponseDTO {
    private Integer enrollmentId;

    // Course info
    private Integer courseId;
    private String courseCode;
    private String courseName;

    // Faculty info
    private Integer facultyId;
    private String facultyName;

    // Academic data
    private BigDecimal attendancePercentage;
    private BigDecimal internalMarks;

    // Student info (useful for faculty-side responses)
    private Integer studentId;
    private String studentName;
    private String studentUsn;

    public EnrollmentResponseDTO() {}

    // ---- Getters & Setters ----

    public Integer getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Integer enrollmentId) { this.enrollmentId = enrollmentId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getFacultyId() { return facultyId; }
    public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public BigDecimal getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(BigDecimal attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public BigDecimal getInternalMarks() { return internalMarks; }
    public void setInternalMarks(BigDecimal internalMarks) { this.internalMarks = internalMarks; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentUsn() { return studentUsn; }
    public void setStudentUsn(String studentUsn) { this.studentUsn = studentUsn; }
}

