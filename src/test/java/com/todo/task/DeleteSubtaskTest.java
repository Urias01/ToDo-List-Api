package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.todo.application.usecase.task.DeleteSubtask;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class DeleteSubtaskTest {

  @InjectMocks
  private DeleteSubtask deleteSubask;

  @Mock
  private ITaskCommandRepository taskCommandRepository;
  @Mock
  private ITaskQueryRepository taskQueryRepository;
  @Mock
  private IUserQueryRepository userQueryRepository;

  @Test
  @DisplayName("Should be able to delete a Subask")
  public void shouldBeAbleToDeleteASubtask() {
    User executor = new User();
    executor.setId("user-123");

    Task task = new Task("title", "description", executor, TaskStatus.PENDING);
    Task subtask = new Task("subtask title", "description", executor, TaskStatus.PENDING);

    task.addSubtask(subtask);

    when(userQueryRepository.findById("user-123")).thenReturn(Optional.of(executor));
    when(taskQueryRepository.findById(task.getId())).thenReturn(Optional.of(task));

    deleteSubask.execute(task.getId(), subtask.getId(), "user-123");

    verify(taskCommandRepository).update(task);
    assertTrue(task.getSubtasks().isEmpty());
  }
}
