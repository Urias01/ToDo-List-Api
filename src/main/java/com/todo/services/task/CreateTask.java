package com.todo.services.task;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todo.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.requests.TaskRequest;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTask {

  private final IUserQueryRepository userQueryRepository;
  private final ITaskCommandRepository taskCommandRepository;

  public String execute(TaskRequest request) {
    Task task = TaskMapper.toEntity(request);

    if (request.userIds() == null || request.userIds().isEmpty()) {
      taskCommandRepository.save(task);
      return task.getId();
    }

    Set<User> users = request.userIds().stream()
        .map(userId -> {
          User user = userQueryRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId));
          return user;
        })
        .collect(Collectors.toSet());

    task.setUsers(users);

    taskCommandRepository.save(task);

    return task.getId();
  }
}
