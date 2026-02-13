package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserUsername(String username, Pageable pageable);

    Optional<Task> findByIdAndUserUsername(Long id, String username);
}
