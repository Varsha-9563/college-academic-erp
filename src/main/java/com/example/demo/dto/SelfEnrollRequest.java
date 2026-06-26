package com.example.demo.dto;

/**
 * PHASE 2 — Request body for student self-enrollment.
 *
 * The student only provides courseId.
 * studentId is extracted from the JWT token (not passed in the request).
 * facultyId is resolved automatically from the course's assigned faculty.
 */
public class SelfEnrollRequest {

    private Integer courseId;

    public SelfEnrollRequest() {}

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
}
