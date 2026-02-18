package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.models.entities.Task;
import com.todo.models.entities.User;
import com.todo.models.requests.TaskRequest;
import com.todo.ports.task.ITaskCommandRepository;
import com.todo.ports.task.ITaskQueryRepository;
import com.todo.ports.user.IUserQueryRepository;
import com.todo.services.task.CreateTask;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {

  @InjectMocks
  private CreateTask createTask;

  @Mock
  private IUserQueryRepository userQueryRepository;
  @Mock
  private ITaskQueryRepository taskQueryRepository;
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
        null, null);

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

  @Test
  @DisplayName("Should create a task with subtask successfully")
  public void shouldCreateTaskWithSubtaskSuccessfully() {
    String userId = "user-123";
    String parentId = "task-123";

    List<String> users = List.of(userId);

    Task parentTask = new Task();
    parentTask.setId(parentId);

    TaskRequest request = new TaskRequest(
        "Test Task",
        "This is a test task",
        users,
        parentId,
        null);

    User user = new User();
    user.setId(userId);

    when(userQueryRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskQueryRepository.findById(parentId)).thenReturn(Optional.of(parentTask));

    ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

    when(taskCommandRepository.save(captor.capture()))
        .thenAnswer(i -> {
          Task t = i.getArgument(0);
          t.setId("generated-task-id");
          return t;
        });

    String taskId = createTask.execute(request);

    Task savedTask = captor.getValue();

    assertEquals("generated-task-id", taskId);
    assertEquals(parentId, savedTask.getParentTask().getId());
    assertTrue(savedTask.getUsers().stream().anyMatch(u -> userId.equals(u.getId())));

    verify(userQueryRepository).findById(userId);
    verify(taskQueryRepository).findById(parentId);
    verify(taskCommandRepository).save(Mockito.any(Task.class));
  }

  @Test
  @DisplayName("Should not create a subtask of subtask")
  public void shouldNotCreateSubtaskOfSubtask() {
    String userId = "user-123";
    String parentId = "task-123";

    List<String> users = List.of(userId);

    Task parentTask = new Task();
    parentTask.setId(parentId);

    Task grandParent = new Task();
    grandParent.setId("grand-parent");

    parentTask.setParentTask(grandParent);

    TaskRequest request = new TaskRequest(
        "Test Task",
        "This is a test task",
        users,
        parentId,
        null);

    User user = new User();
    user.setId(userId);

    when(taskQueryRepository.findById(parentId)).thenReturn(Optional.of(parentTask));

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> createTask.execute(request));

    assertEquals("Cannot set a parent task that is already a subtask",
        ex.getMessage());
  }
}
