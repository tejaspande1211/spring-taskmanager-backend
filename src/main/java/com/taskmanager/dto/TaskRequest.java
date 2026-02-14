package com.taskmanager.dto;

import com.taskmanager.entity.Task;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    private String title;
    private String description;
    private Task.TaskStatus status;
    private Task.TaskPriority priority;
    private LocalDate dueDate;
    private String attachmentUrl;
}
