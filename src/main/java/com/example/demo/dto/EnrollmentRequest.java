package com.example.demo.dto;

import java.math.BigDecimal;

public class EnrollmentRequest {

    private Integer studentId;
    private Integer courseId;
    private Integer facultyId;

    private BigDecimal attendancePercentage;
    private BigDecimal internalMarks;

    public EnrollmentRequest() {}

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public Integer getFacultyId() { return facultyId; }
    public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }

    public BigDecimal getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(BigDecimal attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public BigDecimal getInternalMarks() { return internalMarks; }
    public void setInternalMarks(BigDecimal internalMarks) {
        this.internalMarks = internalMarks;
    }
}
