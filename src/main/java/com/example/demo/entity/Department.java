package com.example.demo.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Department {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Integer deptId;


    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "hod_faculty_id")
    private Faculty hod;

    public Department() {}

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Faculty getHod() {
        return hod;
    }

    public void setHod(Faculty hod) {
        this.hod = hod;
    }
}
