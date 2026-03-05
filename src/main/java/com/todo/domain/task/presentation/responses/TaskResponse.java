package com.todo.domain.task.presentation.responses;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.todo.domain.task.entities.Task;
import com.todo.domain.user.entities.User;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    User createdBy,
    Set<Task> subtasks,
    LocalDateTime finishDate,
    LocalDateTime createdAt) {

}
