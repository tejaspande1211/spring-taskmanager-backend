package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request, String username) {
        log.info("Creating task for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.TaskStatus.TODO);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Task.TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setUser(user);

        Task saved = taskRepository.save(task);
        log.info("Task created successfully: ID {}", saved.getId());

        return mapToResponse(saved);
    }

    @Cacheable(value = "tasks", key = "#username + '_' + #pageable")
    public Page<TaskResponse> getAllTasks(String username, Pageable pageable) {
        log.info("🔥 CACHE MISS - Fetching tasks for user: {}", username);

        return taskRepository.findByUserUsername(username, pageable)
                .map(this::mapToResponse);
    }

    @Cacheable(value = "tasks", key = "#id + '_' + #username")
    public TaskResponse getTaskById(Long id, String username) {
        log.info("🔥 CACHE MISS - Single task ID: {} for user: {}", id, username);

        return taskRepository.findByIdAndUserUsername(id, username)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Task not found: ID " + id));
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public TaskResponse updateTask(Long id, TaskRequest request, String username) {
        log.info("Updating task ID: {} for user: {}", id, username);

        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        Task updated = taskRepository.save(task);
        log.info("Task updated successfully: ID {}", updated.getId());

        return mapToResponse(updated);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void deleteTask(Long id, String username) {
        log.info("Deleting task ID: {} for user: {}", id, username);

        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        taskRepository.delete(task);
        log.info("Task deleted successfully: ID {}", id);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .attachmentUrl(task.getAttachmentUrl())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
