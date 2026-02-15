package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskNotificationEvent implements Serializable {

    private Long taskId;
    private String taskTitle;
    private String username;
    private String userEmail;
}
