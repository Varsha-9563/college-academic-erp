package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Username cannot be empty")
    private String password;

    private Role role;

    // getters & setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
