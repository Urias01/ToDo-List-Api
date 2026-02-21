package com.todo.services.task;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todo.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.requests.TaskRequest;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.task.ITaskQueryRepository;
import com.todo.ports.user.IUserQueryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTask {

  private final IUserQueryRepository userQueryRepository;
  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;

  @Transactional
  public String execute(TaskRequest request, String loggedUserId) {

    User creator = userQueryRepository.findById(loggedUserId)
        .orElseThrow(() -> new NotFoundException("Creator user"));

    Task task = TaskMapper.toEntity(request, creator);

    if (request.userIds() != null && !request.userIds().isEmpty()) {
      Set<User> users = request.userIds().stream()
          .map(userId -> userQueryRepository.findById(userId)
              .orElseThrow(() -> new NotFoundException("User with id " + userId)))
          .collect(Collectors.toSet());

      task.assignUsers(users);
    }

    if (request.taskId() != null) {
      Optional<Task> parentTaskOpt = taskQueryRepository.findById(request.taskId());
      if (parentTaskOpt.isEmpty()) {
        throw new NotFoundException("Parent task with id " + request.taskId());
      }
      Task parent = parentTaskOpt.get();
      parent.addSubtask(task);
      taskCommandRepository.update(parent);
      return task.getId();
    } else {
      task = taskCommandRepository.create(task);
    }

    return task.getId();
  }
}
