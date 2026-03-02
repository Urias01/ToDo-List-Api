package com.todo.application.usecase.task;

import org.springframework.stereotype.Service;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateTask {

  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;
  private final IUserQueryRepository userQueryRepository;

  public String execute(String taskId, TaskRequestUpdate request) {
    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    TaskMapper.updateEntity(task, request);

    taskCommandRepository.update(task);

    return task.getId();
  }
}
