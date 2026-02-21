package com.todo.ports.task;

import com.todo.models.entities.Task;

public interface ITaskCommandRepository {
  Task create(Task task);

  Task update(Task task);
}
