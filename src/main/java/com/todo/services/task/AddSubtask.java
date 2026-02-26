package com.todo.services.task;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todo.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.requests.SubTaskRequest;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.task.ITaskQueryRepository;
import com.todo.ports.user.IUserQueryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddSubtask {

  private final IUserQueryRepository userQueryRepository;
  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;

  @Transactional
  public String execute(SubTaskRequest request, String loggedUserId) {

    User creator = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Creator user"));

    Task newSubTask = TaskMapper.toEntity(request, creator);

    if (request.parentId() == null) {
      throw new IllegalArgumentException("Cannot create subtask without parent task");
    }

    Task parent = taskQueryRepository.findById(request.parentId())
        .orElseThrow(() -> new NotFoundException("Parent task"));

    parent.ensureCanCreateSubtask(creator);

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
