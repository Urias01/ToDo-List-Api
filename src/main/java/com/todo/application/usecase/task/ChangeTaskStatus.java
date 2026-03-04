package com.todo.application.usecase.task;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.user.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChangeTaskStatus {

  private ITaskQueryRepository taskQueryRepository;
  private ITaskCommandRepository taskCommandRepository;
  private IUserQueryRepository userQueryRepository;

  public UUID execute(UUID taskId, TaskStatus status, String loggedUserId) {
    User executor = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Logged user"));

    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    task.assertUserCanModify(executor);

    task.changeStatus(status);

    taskCommandRepository.update(task);

    return task.getId();
  }
}
