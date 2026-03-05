package com.todo.application.usecase.task;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.domain.user.entities.User;
import com.todo.mappers.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewTaskDetails {

  private final ITaskQueryRepository taskQueryRepository;
  private final IUserQueryRepository userQueryRepository;

  public TaskResponse execute(UUID taskId, UUID loggedUserId) {

    User executor = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Logged user"));

    Task task = taskQueryRepository.findDetailsById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    task.assertUserCanView(executor);

    TaskResponse response = TaskMapper.toResponse(task);

    return response;
  }

}
