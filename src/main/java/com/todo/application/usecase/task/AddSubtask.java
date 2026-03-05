package com.todo.application.usecase.task;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.SubTaskRequest;
import com.todo.domain.user.entities.User;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddSubtask {

  private final IUserQueryRepository userQueryRepository;
  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;

  @Transactional
  public UUID execute(SubTaskRequest request, UUID parentId, UUID loggedUserId) {

    User creator = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Creator user"));

    Task newSubTask = TaskMapper.toEntity(request, creator);

    if (parentId == null) {
      throw new IllegalArgumentException("Cannot create subtask without parent task");
    }

    Task parent = taskQueryRepository.findById(parentId)
        .orElseThrow(() -> new NotFoundException("Parent task"));

    parent.assertUserCanModify(creator);

    parent.addSubtask(newSubTask);

    if (request.userIds() != null && !request.userIds().isEmpty()) {
      Set<User> users = request.userIds().stream()
          .map(userId -> userQueryRepository.findById(userId)
              .orElseThrow(() -> new NotFoundException("User with id " + userId)))
          .collect(Collectors.toSet());

      users.forEach(user -> parent.assignResponsibleToSubtask(newSubTask, creator, user));
    }

    taskCommandRepository.update(parent);

    return newSubTask.getId();
  }
}
