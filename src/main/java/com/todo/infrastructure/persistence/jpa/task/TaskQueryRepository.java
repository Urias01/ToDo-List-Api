package com.todo.infrastructure.persistence.jpa.task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.todo.domain.task.entities.Task;
import com.todo.application.ports.task.ITaskQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepository implements ITaskQueryRepository {

  private final TaskRepository taskRepository;

  @Override
  public Optional<Task> findById(UUID id) {
    return taskRepository.findById(id);
  }

  @Override
  public Optional<Task> findDetailsById(UUID id) {
    return taskRepository.findByIdWithDetails(id);
  }

}
