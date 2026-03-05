package com.todo.application.usecase.task;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.mappers.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListTasks {

  private final ITaskQueryRepository queryRepository;

  public List<TaskResponse> execute(UUID loggedUserId) {

    List<Task> tasks = queryRepository.findByCreatorId(loggedUserId);

    List<TaskResponse> response = tasks.stream().map(TaskMapper::toResponse).toList();

    return response;
  }
}
