package com.todo.application.usecase.task;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.user.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteSubtask {

  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;
  private final IUserQueryRepository userQueryRepository;

  public void execute(UUID taskId, UUID subtaskId, UUID loggedUserId) {

    User executor = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Logged user"));

    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("task"));

    task.removeSubtask(subtaskId, executor);

    taskCommandRepository.update(task);

  }

}
