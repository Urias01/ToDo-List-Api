package com.todo.task;

import static org.mockito.Mockito.verify;
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
import com.todo.application.usecase.task.DeleteTask;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class DeleteTaskTest {

  @InjectMocks
  private DeleteTask deleteTask;

  @Mock
  private ITaskCommandRepository taskCommandRepository;
  @Mock
  private ITaskQueryRepository taskQueryRepository;
  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should be able to delete a Task")
  public void shouldBeAbleToDeleteATask() {
    User executor = new User("executor", "executor@mail.com");

    Task task = new Task("title", "description", executor, TaskStatus.PENDING);

    when(userQueryRepository.findById(executor.getId())).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    deleteTask.execute(task.getId(), executor.getId());

    verify(taskCommandRepository).delete(task);
  }
}
