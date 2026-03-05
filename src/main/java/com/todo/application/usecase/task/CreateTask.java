package com.todo.application.usecase.task;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.user.entities.User;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.user.IUserQueryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTask {

  private final IUserQueryRepository userQueryRepository;
  private final ITaskCommandRepository taskCommandRepository;

  @Transactional
  public UUID execute(TaskRequest request, UUID loggedUserId) {

    User creator = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Creator user"));

    Task newTask = TaskMapper.toEntity(request, creator);

    if (request.userIds() != null && !request.userIds().isEmpty()) {
      Set<User> users = request.userIds().stream()
          .map(userId -> userQueryRepository.findById(userId)
              .orElseThrow(() -> new NotFoundException("User with id " + userId)))
          .collect(Collectors.toSet());

      users.forEach(user -> newTask.addResponsible(creator, user));
    }

    Task savedTask = taskCommandRepository.create(newTask);
    return savedTask.getId();
  }
}
