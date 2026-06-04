//package com.example.demo.controller;
//
//import com.example.demo.dto.CreateUserRequest;
//import com.example.demo.security.JwtUtil;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(AuthenticationManager authenticationManager,
//                          JwtUtil jwtUtil) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }
//    @PostMapping("/register-admin")
//    public String registerAdmin(@RequestBody CreateUserRequest request) {
//
//        if (userRepository.existsByRole(Role.ADMIN)) {
//            throw new RuntimeException("Admin already exists");
//        }
//
//        User user = new User();
//
//        user.setUsername(request.getUsername());
//
//        user.setPasswordHash(
//                passwordEncoder.encode(request.getPassword())
//        );
//
//        user.setRole(Role.ADMIN);
//
//        user.setIsActive(true);
//
//        userRepository.save(user);
//
//        return "Admin created successfully";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody CreateUserRequest request) {
//
//        System.out.println("LOGIN HIT");
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//
//        System.out.println("AUTH SUCCESS");
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        String role = userDetails.getAuthorities()
//                .iterator()
//                .next()
//                .getAuthority()
//                .replace("ROLE_", "");
//
//        return jwtUtil.generateToken(
//                userDetails.getUsername(),
//                role
//        );
//    }
//}
package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register-admin")
    public String registerAdmin(@Valid @RequestBody CreateUserRequest request) {

        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new RuntimeException("Admin already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.ADMIN);

        user.setIsActive(true);

        userRepository.save(user);

        return "Admin created successfully";
    }



    @PostMapping("/login")
    public String login(@Valid @RequestBody CreateUserRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return jwtUtil.generateToken(
                userDetails.getUsername(),
                role
        );
    }
}