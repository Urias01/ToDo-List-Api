package com.todo.application.usecase.task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.domain.task.entities.Task;
import com.todo.domain.user.entities.User;
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

  public UUID execute(TaskRequestUpdate request, UUID taskId, UUID loggedUserId) {
    Optional<User> executor = userQueryRepository.findById(loggedUserId);

    if (executor.isEmpty()) {
      throw new NotFoundException("loggeder user");
    }

    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    task.assertUserCanModify(executor.get());

    TaskMapper.updateEntity(task, request);

    taskCommandRepository.update(task);

    return task.getId();
  }
}
