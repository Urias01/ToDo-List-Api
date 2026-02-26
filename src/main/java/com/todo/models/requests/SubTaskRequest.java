package com.todo.models.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todo.models.enums.TaskStatus;
import org.springframework.util.StringUtils;

public record SubTaskRequest(String title,
    String description,
    List<String> userIds,
    String parentId,
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