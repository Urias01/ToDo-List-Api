package com.todo.application.ports.task;

import com.todo.domain.task.entities.Task;

public interface ITaskCommandRepository {
  Task create(Task task);

  Task update(Task task);

  void delete(Task task);
}
