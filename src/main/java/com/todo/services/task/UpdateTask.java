package com.todo.services.task;

import org.springframework.stereotype.Service;

import com.todo.exceptions.NotFoundException;
import com.todo.mappers.TaskMapper;
import com.todo.models.entities.Task;
import com.todo.models.requests.TaskRequestUpdate;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.task.ITaskQueryRepository;
import com.todo.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateTask {

  private final ITaskCommandRepository taskCommandRepository;
  private final ITaskQueryRepository taskQueryRepository;
  private final IUserQueryRepository userQueryRepository;

  public String execute(String taskId, TaskRequestUpdate request) {
    Task task = taskQueryRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task"));

    TaskMapper.updateEntity(task, request);

    taskCommandRepository.update(task);

    return task.getId();
  }
}
