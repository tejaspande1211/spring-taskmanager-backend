package com.taskmanager.service;

import com.taskmanager.config.RabbitMQConfig;
import com.taskmanager.dto.TaskNotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @RabbitListener(queues = RabbitMQConfig.TASK_NOTIFICATION_QUEUE)
    public void handleTaskCreatedNotification(TaskNotificationEvent event) {
        log.info("[ASYNC NOTIFICATION] Email sent for task '{}' (ID: {}) to user {} ({})",
                event.getTaskTitle(), event.getTaskId(), event.getUsername(), event.getUserEmail());
    }
}
