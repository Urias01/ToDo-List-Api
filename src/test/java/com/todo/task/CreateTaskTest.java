package com.todo.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
  @DisplayName("Should create a task with responsible user successfully")
  void shouldCreateTaskSuccessfully() {

    String loggedUserId = "creator-1";
    String responsibleId = "user-123";

    TaskRequest request = new TaskRequest(
        "Test Task",
        "Description",
        List.of(responsibleId),
        null,
        null);

    User creator = new User();
    creator.setId(loggedUserId);

    User responsible = new User();
    responsible.setId(responsibleId);

    when(userQueryRepository.findById(loggedUserId))
        .thenReturn(Optional.of(creator));

    when(userQueryRepository.findById(responsibleId))
        .thenReturn(Optional.of(responsible));

    when(taskCommandRepository.create(any(Task.class)))
        .thenAnswer(invocation -> {
          Task task = invocation.getArgument(0);
          ReflectionTestUtils.setField(task, "id", "generated-task-id");
          return task;
        });

    String taskId = createTask.execute(request, loggedUserId);

    assertEquals("generated-task-id", taskId);

    verify(taskCommandRepository).create(argThat(task -> task.getUsers().stream()
        .anyMatch(u -> responsibleId.equals(u.getId()))));
  }

  @Test
  @DisplayName("Should create a subtask successfully")
  void shouldCreateTaskWithSubtaskSuccessfully() {

    String loggedUserId = "creator-1";
    String parentId = "parent-123";

    User creator = new User();
    creator.setId(loggedUserId);

    Task parentTask = new Task("Parent", "Desc", creator, null);
    ReflectionTestUtils.setField(parentTask, "id", parentId);

    TaskRequest request = new TaskRequest(
        "Subtask",
        "Sub description",
        null,
        parentId,
        null);

    when(userQueryRepository.findById(loggedUserId))
        .thenReturn(Optional.of(creator));

    when(taskQueryRepository.findById(parentId))
        .thenReturn(Optional.of(parentTask));

    when(taskCommandRepository.update(any(Task.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    createTask.execute(request, loggedUserId);

    assertEquals(1, parentTask.getSubtasks().size());

    Task createdSubtask = parentTask.getSubtasks().iterator().next();

    assertEquals(parentTask, createdSubtask.getParentTask());

    verify(taskCommandRepository).update(parentTask);
  }

  @Test
  @DisplayName("Should not create subtask if parent is already a subtask")
  void shouldNotCreateSubtaskOfSubtask() {

    String loggedUserId = "creator-1";
    String parentId = "parent-123";

    User creator = new User();
    creator.setId(loggedUserId);

    Task grandParent = new Task("Grand", "Desc", creator, null);
    Task parentTask = new Task("Parent", "Desc", creator, null);

    grandParent.addSubtask(parentTask); // parentTask já tem parent

    ReflectionTestUtils.setField(parentTask, "id", parentId);

    TaskRequest request = new TaskRequest(
        "Invalid Subtask",
        "Desc",
        null,
        parentId,
        null);

    when(userQueryRepository.findById(loggedUserId))
        .thenReturn(Optional.of(creator));

    when(taskQueryRepository.findById(parentId))
        .thenReturn(Optional.of(parentTask));

    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> createTask.execute(request, loggedUserId));

    assertEquals("Subtasks cannot have their own subtasks", ex.getMessage());
  }
}