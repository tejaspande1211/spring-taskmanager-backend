package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Boolean active;
    private LocalDateTime createdAt;
}

