package com.todo.ports.task;

import java.util.Optional;

import com.todo.models.entities.Task;

public interface ITaskQueryRepository {
  Optional<Task> findById(String id);
}
