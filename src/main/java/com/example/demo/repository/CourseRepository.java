package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findByDepartment_DeptId(Integer deptId);

    boolean existsByCourseCode(String courseCode);
}
