package com.todo.infrastructure.persistence.jpa.task;

import org.springframework.stereotype.Repository;

import com.todo.models.entities.Task;
import com.todo.ports.task.ITaskCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskCommandRepository implements ITaskCommandRepository {
  
  private final TaskRepository taskRepository;

  @Override
  public Task save(Task task) {
    return taskRepository.save(task);
  }
  
}
