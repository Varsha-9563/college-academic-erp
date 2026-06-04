//package com.example.demo.dto;
//
//public class CreateDepartmentRequest {
//
//    private String departmentName;
//
//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }
//}
package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentRequest {
    @NotBlank
    private String departmentName;

    public DepartmentRequest() {}

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}

