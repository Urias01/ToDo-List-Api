package com.todo.application.ports.task;

import java.util.Optional;
import java.util.UUID;

import com.todo.domain.task.entities.Task;

public interface ITaskQueryRepository {
  Optional<Task> findById(UUID id);
}
