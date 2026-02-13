package com.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminUserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Boolean active;
    private LocalDateTime createdAt;
}
