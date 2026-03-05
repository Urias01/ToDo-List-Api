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
import com.todo.application.usecase.task.UpdateTask;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.exception.UserNotAllowedToPerformActionInTask;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class UpdateTaskTest {

  @InjectMocks
  private UpdateTask updateTask;

  @Mock
  private ITaskCommandRepository taskCommandRepository;

  @Mock
  private ITaskQueryRepository taskQueryRepository;

  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should be able to update a task")
  public void shouldBeAbleToUpdateUpdateATask() {
    User creator = new User("creator", "creator@mail.com");
    Task task = new Task("Task", "description", creator, TaskStatus.IN_PROGRESS);

    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(creator.getId())).thenReturn(Optional.of(creator));

    when(taskQueryRepository.findById(taskId)).thenReturn(Optional.of(task));

    TaskRequestUpdate request = new TaskRequestUpdate("Updated Title", "new description");

    UUID result = updateTask.execute(request, taskId, creator.getId());

    verify(taskCommandRepository).update(task);
    assertEquals(task.getId(), result);
  }

  @Test
  @DisplayName("Should throw when user not found")
  void shouldThrowWhenUserNotFound() {
    User creator = new User("creator", "creator@mail.com");

    when(userQueryRepository.findById(creator.getId()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> updateTask.execute(new TaskRequestUpdate("Title", "Description"), UUID.randomUUID(), creator.getId()));

    verifyNoInteractions(taskQueryRepository);
  }

  @Test
  @DisplayName("Should throw when task not found")
  void shouldThrowWhenTaskNotFound() {
    User creator = new User("creator", "creator@mail.com");

    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(creator.getId()))
        .thenReturn(Optional.of(creator));

    when(taskQueryRepository.findById(taskId))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> updateTask.execute(new TaskRequestUpdate("title", "description"), taskId, creator.getId()));
    verifyNoInteractions(taskCommandRepository);
  }

  @Test
  @DisplayName("Should throw when user cannot modify")
  void shouldThrowWhenUserCannotModify() {
    User creator = new User("creator", "creator@mail.com");
    User anotherUser = new User();
    Task task = new Task("Title", "Description", creator, TaskStatus.PENDING);
    UUID taskId = UUID.randomUUID();

    when(userQueryRepository.findById(creator.getId()))
        .thenReturn(Optional.of(anotherUser));

    when(taskQueryRepository.findById(taskId))
        .thenReturn(Optional.of(task));

    assertThrows(UserNotAllowedToPerformActionInTask.class,
        () -> updateTask.execute(new TaskRequestUpdate("title", "description"), taskId, creator.getId()));
    verifyNoInteractions(taskCommandRepository);
  }
}
