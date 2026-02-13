package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // CREATE
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody TaskRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                taskService.createTask(request, authentication.getName())
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                taskService.getAllTasks(authentication.getName(), pageable)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request, authentication.getName())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        taskService.deleteTask(id, authentication.getName());
        return ResponseEntity.ok("Task deleted successfully");
    }
}
