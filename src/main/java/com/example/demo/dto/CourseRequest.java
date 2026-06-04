package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CourseRequest {
    @NotBlank
    private String courseCode;
    @NotBlank
    private String courseName;

    private Integer deptId;

    public CourseRequest() {}

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
}
