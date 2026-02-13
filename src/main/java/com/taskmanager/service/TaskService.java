package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(TaskRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(
                request.getStatus() != null ? request.getStatus() : Task.TaskStatus.TODO
        );
        task.setPriority(
                request.getPriority() != null ? request.getPriority() : Task.TaskPriority.MEDIUM
        );
        task.setDueDate(request.getDueDate());
        task.setUser(user);

        Task saved = taskRepository.save(task);

        return mapToResponse(saved);
    }

    // 🔥 CACHE READ
    @Cacheable(
            value = "tasks",
            key = "#username + '_' + #pageable.pageNumber + '_' + #pageable.pageSize"
    )
    public Page<TaskResponse> getAllTasks(String username, Pageable pageable) {

        System.out.println("Fetching tasks from DB...");

        return taskRepository.findByUserUsername(username, pageable)
                .map(this::mapToResponse);
    }

    // 🔥 EVICT CACHE ON UPDATE
    @CacheEvict(
            value = "tasks",
            allEntries = true
    )
    public TaskResponse updateTask(Long id, TaskRequest request, String username) {

        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        if (request.getTitle() != null)
            task.setTitle(request.getTitle());
        if (request.getDescription() != null)
            task.setDescription(request.getDescription());
        if (request.getStatus() != null)
            task.setStatus(request.getStatus());
        if (request.getPriority() != null)
            task.setPriority(request.getPriority());
        if (request.getDueDate() != null)
            task.setDueDate(request.getDueDate());

        Task updated = taskRepository.save(task);

        return mapToResponse(updated);
    }

    // 🔥 EVICT CACHE ON DELETE
    @CacheEvict(
            value = "tasks",
            allEntries = true
    )
    public void deleteTask(Long id, String username) {

        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        taskRepository.delete(task);
    }

    private TaskResponse mapToResponse(Task task) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
