package com.todo.application.usecase.task;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.user.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateSubtask {

  private ITaskCommandRepository taskCommandRepository;
  private ITaskQueryRepository taskQueryRepository;
  private IUserQueryRepository userQueryRepository;

  public UUID execute(UUID taskId, UUID subtaskId, TaskRequestUpdate request, String loggedUserId) {

    User executor = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("logged user"));

    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    task.updateSubtask(subtaskId, request.title(), request.description(), executor);

    taskCommandRepository.update(task);

    return task.getId();
  }
}
