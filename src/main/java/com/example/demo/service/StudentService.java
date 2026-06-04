package com.example.demo.service;

import com.example.demo.entity.Department;
import com.example.demo.entity.Role;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          DepartmentRepository departmentRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student createStudent(String name,
                                 String usn,
                                 String email,
                                 Integer deptId,
                                 Integer batchYear,
                                 String username,
                                 String rawPassword) {

        if (studentRepository.existsByUsn(usn)) {
            throw new RuntimeException("USN already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // 🔥 Create User
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(Role.STUDENT);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // 🔥 Create Student
        Student student = new Student();
        student.setName(name);
        student.setUsn(usn);
        student.setEmail(email);
        student.setDepartment(department);
        student.setUser(savedUser);
        student.setBatchYear(batchYear);

        return studentRepository.save(student);
    }
    public Student getStudentById(Integer studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // 3️⃣ Get All Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 4️⃣ Get Students by Department
    public List<Student> getStudentsByDepartment(Integer deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new RuntimeException("Department not found");
        }
        return studentRepository.findByDepartment_DeptId(deptId);
    }

    // 5️⃣ Delete Student
    public void deleteStudent(Integer studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Optional: deactivate user instead of deleting
        User user = student.getUser();
        if (user != null) {
            user.setIsActive(false);
            userRepository.save(user);
        }

        studentRepository.delete(student);
    }
}
