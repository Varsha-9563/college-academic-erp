package com.example.demo.service;
import com.example.demo.entity.Role;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create User
    public User createUser(String username, String rawPassword, Role role) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);

        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        user.setRole(role);   // ✅ directly set enum

        user.setIsActive(true);

        return userRepository.save(user);
    }

    // Get User by Username
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
