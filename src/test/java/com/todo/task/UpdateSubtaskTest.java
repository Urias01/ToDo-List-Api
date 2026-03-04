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
import com.todo.application.usecase.task.UpdateSubtask;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class UpdateSubtaskTest {

  @InjectMocks
  private UpdateSubtask updateSubtask;

  @Mock
  private ITaskCommandRepository taskCommandRepository;

  @Mock
  private ITaskQueryRepository taskQueryRepository;

  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should be able to update a subtask")
  public void shouldBeAbleToUpdateUpdateASubtask() {
    User creator = new User();

    Task task = new Task("Task", "description", creator, TaskStatus.IN_PROGRESS);
    Task subtask = new Task("subtask", "description", creator, TaskStatus.IN_PROGRESS);
    task.addSubtask(subtask);

    when(userQueryRepository.findById("loggedUser-123")).thenReturn(Optional.of(creator));

    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    TaskRequestUpdate request = new TaskRequestUpdate("Updated Title", "new description");

    UUID result = updateSubtask.execute(task.getId(), subtask.getId(), request, "loggedUser-123");

    verify(taskCommandRepository).update(task);
    assertEquals(subtask.getId(), result);
  }

  @Test
  @DisplayName("Should throw when user not found")
  void shouldThrowWhenUserNotFound() {
    when(userQueryRepository.findById("user-id"))
        .thenReturn(Optional.empty());

    UUID taskId = UUID.randomUUID();
    UUID subtaskId = UUID.randomUUID();

    assertThrows(NotFoundException.class,
        () -> updateSubtask.execute(taskId, subtaskId, new TaskRequestUpdate("Title", "Description"), "user-id"));

    verifyNoInteractions(taskQueryRepository);
  }

  @Test
  @DisplayName("Should throw when subtask not found")
  void shouldThrowWhenSubtaskNotFound() {
    User user = new User();

    UUID taskId = UUID.randomUUID();
    UUID subtaskId = UUID.randomUUID();

    when(userQueryRepository.findById("user-id"))
        .thenReturn(Optional.of(user));

    when(taskQueryRepository.findById(taskId))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> updateSubtask.execute(taskId, subtaskId, new TaskRequestUpdate("title", "description"), "user-id"));
    verifyNoInteractions(taskCommandRepository);
  }
}
