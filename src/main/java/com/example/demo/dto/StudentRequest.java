package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String usn;
    @NotBlank
    @Email
    private String email;

    private Integer deptId;
    private Integer batchYear;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public StudentRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsn() { return usn; }
    public void setUsn(String usn) { this.usn = usn; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }

    public Integer getBatchYear() { return batchYear; }
    public void setBatchYear(Integer batchYear) { this.batchYear = batchYear; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
