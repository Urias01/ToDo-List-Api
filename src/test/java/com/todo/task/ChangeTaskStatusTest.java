package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.task.ChangeTaskStatus;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.exception.TaskAlreadyFinishedException;
import com.todo.domain.task.presentation.requests.ChangeStatusRequest;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class ChangeTaskStatusTest {

  @InjectMocks
  private ChangeTaskStatus changeTaskStatus;

  @Mock
  private ITaskCommandRepository taskCommandRepository;
  @Mock
  private ITaskQueryRepository taskQueryRepository;
  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should change task status of pending to in progress")
  public void shouldChangeTaskStatusOfPendingToInProgress() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.PENDING);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.IN_PROGRESS);
    changeTaskStatus.execute(taskId, request, executor.getId());

    verify(taskCommandRepository).update(task);
    assertEquals(task.getStatus(), TaskStatus.IN_PROGRESS);
  }

  @Test
  @DisplayName("Should change task status of in progress to finished")
  public void shouldChangeTaskStatusOfInProgressToFinished() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.IN_PROGRESS);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.FINISHED);
    changeTaskStatus.execute(taskId, request, executor.getId());

    verify(taskCommandRepository).update(task);
    assertEquals(task.getStatus(), TaskStatus.FINISHED);
  }

  @Test
  @DisplayName("Should change task status of in progress to cancelled")
  public void shouldChangeTaskStatusOfInProgressToCancelled() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.IN_PROGRESS);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.CANCELLED);
    changeTaskStatus.execute(taskId, request, executor.getId());

    verify(taskCommandRepository).update(task);
    assertEquals(task.getStatus(), TaskStatus.CANCELLED);
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to cancelled")
  public void shouldThrowExceptionWhenTryChangeTaskStatusOfInProgressToCancelled() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.CANCELLED);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeTaskStatus.execute(taskId, request, executor.getId()));

    verifyNoInteractions(taskCommandRepository);
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to pending")
  public void shouldThrowExceptionWhenTryChangeTaskStatusOfInProgressToPending() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.PENDING);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeTaskStatus.execute(taskId, request, executor.getId()));

    verifyNoInteractions(taskCommandRepository);
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to finished")
  public void shouldThrowExceptionWhenTryChangeTaskStatusOfInProgressToFinished() {
    User executor = new User("user", "emaiL@test.com");

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.FINISHED);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeTaskStatus.execute(taskId, request, executor.getId()));

    verifyNoInteractions(taskCommandRepository);
  }
}
