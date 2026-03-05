package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.task.ChangeSubtaskStatus;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.exception.TaskAlreadyFinishedException;
import com.todo.domain.task.presentation.requests.ChangeStatusRequest;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class ChangeSubtaskStatusTest {

  @InjectMocks
  private ChangeSubtaskStatus changeSubtaskStatus;

  @Mock
  private ITaskCommandRepository taskCommandRepository;
  @Mock
  private ITaskQueryRepository taskQueryRepository;
  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should change task status of pending to in progress")
  public void shouldChangeSubtaskStatusOfPendingToInProgress() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.PENDING);
    Task subtask = new Task("Title", "description", executor, TaskStatus.PENDING);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.IN_PROGRESS);
    changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString());

    verify(taskCommandRepository).update(task);
    assertEquals(subtask.getStatus(), TaskStatus.IN_PROGRESS);
  }

  @Test
  @DisplayName("Should change task status of in progress to finished")
  public void shouldChangeSubtaskStatusOfInProgressToFinished() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.PENDING);
    Task subtask = new Task("Title", "description", executor, TaskStatus.PENDING);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.FINISHED);
    changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString());

    verify(taskCommandRepository).update(task);
    assertEquals(subtask.getStatus(), TaskStatus.FINISHED);
  }

  @Test
  @DisplayName("Should change subtask status of in progress to cancelled")
  public void shouldChangeSubtaskStatusOfInProgressToCancelled() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.IN_PROGRESS);
    Task subtask = new Task("Title", "description", executor, TaskStatus.PENDING);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.CANCELLED);
    changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString());

    verify(taskCommandRepository).update(task);
    assertEquals(TaskStatus.CANCELLED, subtask.getStatus());
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to cancelled")
  public void shouldThrowExceptionWhenTryChangeSubtaskStatusOfInProgressToCancelled() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    Task subtask = new Task("Title", "description", executor, TaskStatus.FINISHED);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.CANCELLED);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString()));

    verifyNoInteractions(taskCommandRepository);
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to pending")
  public void shouldThrowExceptionWhenTryChangeSubtaskStatusOfInProgressToPending() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    Task subtask = new Task("Title", "description", executor, TaskStatus.FINISHED);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.PENDING);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString()));

    verifyNoInteractions(taskCommandRepository);
  }

  @Test
  @DisplayName("Should throw when try change task status of finished to finished")
  public void shouldThrowExceptionWhenTryChangeSubtaskStatusOfInProgressToFinished() {
    User executor = new User();

    Task task = new Task("Title", "description", executor, TaskStatus.FINISHED);
    Task subtask = new Task("Title", "description", executor, TaskStatus.FINISHED);
    task.addSubtask(subtask);

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    ChangeStatusRequest request = new ChangeStatusRequest(TaskStatus.FINISHED);
    assertThrows(TaskAlreadyFinishedException.class,
        () -> changeSubtaskStatus.execute(task.getId(), subtask.getId(), request, anyString()));

    verifyNoInteractions(taskCommandRepository);
  }
}
