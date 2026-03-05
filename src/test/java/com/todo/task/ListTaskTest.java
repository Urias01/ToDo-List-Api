package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.usecase.task.ListTasks;
import com.todo.domain.task.entities.Task;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.domain.user.entities.User;

@ExtendWith(MockitoExtension.class)
public class ListTaskTest {

  @InjectMocks
  private ListTasks listTasks;

  @Mock
  private ITaskQueryRepository taskQueryRepository;

  @Test
  @DisplayName("should list parent tasks with subtasks")
  void shouldListParentTasks() {

    User user = new User("John", "john@mail.com");
    UUID userId = user.getId();

    Task parent = new Task("Task A", "desc", user, TaskStatus.PENDING);
    Task subtask = new Task("Subtask A", "desc", user, TaskStatus.PENDING);

    parent.addSubtask(subtask);

    when(taskQueryRepository.findByCreatorId(userId))
        .thenReturn(List.of(parent));

    List<TaskResponse> result = listTasks.execute(userId);

    assertEquals(1, result.size());
  }
}
