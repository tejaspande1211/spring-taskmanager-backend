package com.taskmanager.controller;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.LoginResponse;
import com.taskmanager.dto.UserResponse;
import com.taskmanager.entity.User;
import com.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}

