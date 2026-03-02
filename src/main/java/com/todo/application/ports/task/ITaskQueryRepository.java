package com.todo.application.ports.task;

import java.util.Optional;

import com.todo.domain.task.entities.Task;

public interface ITaskQueryRepository {
  Optional<Task> findById(String id);
}
