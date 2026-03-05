package com.todo.domain.task.presentation.requests;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todo.domain.task.enums.TaskStatus;
import org.springframework.util.StringUtils;

public record SubTaskRequest(String title,
    String description,
    List<UUID> userIds,
    @JsonProperty(defaultValue = "PENDING") TaskStatus status) {
  public SubTaskRequest {
    if (!StringUtils.hasText(title)) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }
    if (status == null) {
      status = TaskStatus.PENDING;
    }
  }
}