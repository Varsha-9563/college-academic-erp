package com.example.demo.dto;

/**
 * PHASE 2 — Response DTO for course listings.
 *
 * Used by GET /api/student/my-courses and GET /api/faculty/my-courses
 * so we don't return the full Course entity with lazy Department inside.
 */
public class CourseResponseDTO {

    private Integer courseId;
    private String courseCode;
    private String courseName;
    private String departmentName;

    public CourseResponseDTO() {}

    public CourseResponseDTO(Integer courseId, String courseCode,
                             String courseName, String departmentName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.departmentName = departmentName;
    }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
