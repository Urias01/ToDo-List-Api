package com.todo.mappers;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.SubTaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.domain.user.entities.User;
import com.todo.domain.task.enums.TaskStatus;

public class TaskMapper {

  public static Task toEntity(TaskRequest request, User creator) {
    TaskStatus status = request.status() != null ? request.status() : TaskStatus.PENDING;
    Task task = new Task(request.title(), request.description(), creator, status);
    return task;
  }

  public static Task toEntity(SubTaskRequest request, User creator) {
    TaskStatus status = request.status() != null ? request.status() : TaskStatus.PENDING;
    Task task = new Task(request.title(), request.description(), creator, status);
    return task;
  }

  public static void updateEntity(Task task, TaskRequestUpdate request) {
    task.rename(request.title());
    task.updateDescription(request.description());
  }

  public static TaskResponse toResponse(Task task) {
    return new TaskResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getCreatedBy(),
        task.getSubtasks(),
        task.getFinishDate(),
        task.getCreatedAt());
  }
}
