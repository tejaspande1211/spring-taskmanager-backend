package com.taskmanager.service;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.LoginResponse;
import com.taskmanager.dto.UserResponse;
import com.taskmanager.entity.User;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // ✅ Auto-injects all final fields
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ✅ Added
    private final JwtService jwtService;  // ✅ Your own service import

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // ✅ Fixed
        user.setRole("USER");
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getFullName(), savedUser.getRole(), savedUser.getActive(),
                savedUser.getCreatedAt()
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());  // ✅ Fixed import/call
        return new LoginResponse(token);
    }
}
