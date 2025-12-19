package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.requests.TaskRequest;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.user.IUserQueryRepository;
import com.todo.services.task.CreateTask;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {
  
  @InjectMocks
  private CreateTask createTask;

  @Mock
  private IUserQueryRepository userQueryRepository;
  @Mock
  private ITaskCommandRepository taskCommandRepository;

  @Test
  @DisplayName("Should create a task  with responsible user successfully")
  public void shouldCreateTaskSuccessfully() {
    List<String> users = List.of("user-123");
    
    TaskRequest request = new TaskRequest(
      "Test Task",
      "This is a test task",
      users,
      null
    );

    User user = new User();
    user.setId("user-123");

    when(userQueryRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(taskCommandRepository.save(Mockito.any())).thenAnswer(invocation -> {
      Task task = invocation.getArgument(0);
      assertTrue(task.getUsers().stream().anyMatch(u -> "user-123".equals(u.getId())));
      task.setId("generated-task-id");
      return task;
    });
    
    String taskId = createTask.execute(request);
    
    assertEquals("generated-task-id", taskId);
    verify(userQueryRepository).findById("user-123");
    verify(taskCommandRepository).save(Mockito.any(Task.class));
  }
}
