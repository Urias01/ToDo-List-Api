package com.todo.mappers;

import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.enums.TaskStatus;
import com.todo.models.requests.TaskRequest;
import com.todo.models.requests.TaskRequestUpdate;

public class TaskMapper {

  public static Task toEntity(TaskRequest request, User creator) {
    TaskStatus status = request.status() != null ? request.status() : TaskStatus.PENDING;
    Task task = new Task(request.title(), request.description(), creator, status);
    return task;
  }

  public static void updateEntity(Task task, TaskRequestUpdate request) {
    task.rename(request.title());
    task.updateDescription(request.description());
    if (request.status() != null) {
      task.changeStatus(TaskStatus.valueOf(request.status()));
    }
  }
}
