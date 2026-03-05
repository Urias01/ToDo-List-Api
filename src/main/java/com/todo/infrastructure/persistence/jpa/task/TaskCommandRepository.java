package com.todo.infrastructure.persistence.jpa.task;

import org.springframework.stereotype.Repository;

import com.todo.domain.task.entities.Task;
import com.todo.application.ports.task.ITaskCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskCommandRepository implements ITaskCommandRepository {

  private final TaskRepository taskRepository;

  @Override
  public Task create(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Task update(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public void delete(Task task) {
    taskRepository.delete(task);
  }

}
