package com.example.demo.entity;

import jakarta.persistence.*;

/**
 * PHASE 2 — Added assignedFaculty (ManyToOne → faculty).
 *
 * Why: for student self-enrollment, when a student picks a courseId,
 * the system needs to auto-resolve which faculty teaches it.
 * We store this as a direct FK on the course: course.faculty_id
 *
 * DB migration needed — run this SQL once:
 *   ALTER TABLE course ADD COLUMN faculty_id INT NULL;
 *   ALTER TABLE course ADD CONSTRAINT fk_course_faculty
 *       FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id);
 *
 * Also update application.yml temporarily:
 *   ddl-auto: validate   ← change to update for one run, then revert to validate
 *
 * OR just run the ALTER TABLE manually (safer for production-style workflow).
 */
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    // ✅ PHASE 2 — which faculty is assigned to teach this course
    // This is how student self-enrollment resolves facultyId automatically
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = true)
    private Faculty assignedFaculty;

    public Course() {}

    public Course(String courseCode, String courseName, Department department) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
    }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Faculty getAssignedFaculty() { return assignedFaculty; }
    public void setAssignedFaculty(Faculty assignedFaculty) { this.assignedFaculty = assignedFaculty; }
}
