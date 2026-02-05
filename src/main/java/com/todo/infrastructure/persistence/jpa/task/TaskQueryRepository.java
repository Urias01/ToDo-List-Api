package com.todo.infrastructure.persistence.jpa.task;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.todo.models.entities.Task;
import com.todo.ports.task.ITaskQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepository implements ITaskQueryRepository {

  private final TaskRepository taskRepository;

  @Override
  public Optional<Task> findById(String id) {
    return taskRepository.findById(id);
  }

}
