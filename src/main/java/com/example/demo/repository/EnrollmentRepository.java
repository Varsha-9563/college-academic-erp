package com.example.demo.repository;

import com.example.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudent_StudentId(Integer studentId);

    List<Enrollment> findByCourse_CourseId(Integer courseId);

    List<Enrollment> findByFaculty_FacultyId(Integer facultyId);

    Optional<Enrollment> findByStudent_StudentIdAndCourse_CourseId(
            Integer studentId,
            Integer courseId
    );

    boolean existsByStudent_StudentIdAndCourse_CourseId(
            Integer studentId,
            Integer courseId
    );

    // 🔥 NEW — For course deletion validation
    boolean existsByCourse_CourseId(Integer courseId);
}
