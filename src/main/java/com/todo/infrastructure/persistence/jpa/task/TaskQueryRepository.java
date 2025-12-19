package com.todo.infrastructure.persistence.jpa.task;

import org.springframework.stereotype.Repository;

import com.todo.ports.task.ITaskQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepository implements ITaskQueryRepository {
  
  private final TaskRepository taskRepository;
}
