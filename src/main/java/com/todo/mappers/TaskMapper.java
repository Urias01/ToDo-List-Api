package com.todo.mappers;

import com.todo.models.entities.Task;
import com.todo.models.enums.TaskStatus;
import com.todo.models.requests.TaskRequest;

public class TaskMapper {
  
  public static Task toEntity(TaskRequest request) {
    Task task = new Task();
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setStatus(request.status() != null ? request.status() : TaskStatus.PENDING);
    return task;
  }
}
