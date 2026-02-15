package com.taskmanager.service;

import com.taskmanager.config.RabbitMQConfig;
import com.taskmanager.dto.TaskNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishTaskCreatedEvent(TaskNotificationEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TASK_EXCHANGE,
                RabbitMQConfig.TASK_NOTIFICATION_ROUTING_KEY,
                event
        );

        log.info("Task creation event published. Task ID: {}, user: {}", event.getTaskId(), event.getUsername());
    }
}
