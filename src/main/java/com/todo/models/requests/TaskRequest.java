package com.todo.models.requests;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.todo.models.enums.TaskStatus;

public record TaskRequest(
    String title,
    String description,
    List<String> userIds,
    @JsonProperty(defaultValue = "PENDING") TaskStatus status) {
  public TaskRequest {
    if(!StringUtils.hasText(title)) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }
    if (status == null) {
      status = TaskStatus.PENDING;
    }
  }
}
