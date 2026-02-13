package com.taskmanager.service;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.LoginResponse;
import com.taskmanager.dto.UserResponse;
import com.taskmanager.entity.User;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public UserResponse registerUser(User user) {
        String username = user.getUsername().toLowerCase();
        String email = user.getEmail().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setActive(true);
        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                savedUser.getActive(),
                savedUser.getCreatedAt()
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername().toLowerCase())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid username or password")
                );
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }
}

