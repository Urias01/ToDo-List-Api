package com.todo.ports.task;

import com.todo.models.entities.Task;

public interface ITaskCommandRepository {
  Task save(Task task);
}
