package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.task.ViewTaskDetails;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class ViewTaskDetailsTest {

  @InjectMocks
  private ViewTaskDetails viewTaskDetails;

  @Mock
  private ITaskQueryRepository taskQueryRepository;

  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should be able to view task details")
  public void shouldBeAbleToViewTaskDetails() {
    User executor = new User("executor", "executor@mail.com");

    Task task = new Task("title", "description", executor, TaskStatus.PENDING);
    Task subtask = new Task("title sub", "description", executor, TaskStatus.PENDING);
    UUID taskId = task.getId();

    task.addSubtask(subtask);

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findDetailsById(taskId)).thenReturn(Optional.of(task));

    TaskResponse response = viewTaskDetails.execute(taskId, executor.getId());

    assertNotNull(response);
    verify(taskQueryRepository).findDetailsById(taskId);
  }
}
