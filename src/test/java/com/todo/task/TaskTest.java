package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.todo.domain.task.entities.Task;
import com.todo.domain.user.entities.User;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.exception.CannotAddSubtaskToCancelledTaskException;
import com.todo.domain.task.exception.CannotBeNullException;
import com.todo.domain.task.exception.SubtaskAlreadyHasParentException;
import com.todo.domain.task.exception.TaskAlreadyFinishedException;

public class TaskTest {

  @Test
  @DisplayName("Should be able to rename a title")
  void shouldBeAbleToRenameATitle() {
    User creator = new User();

    Task task = new Task("Title", "Description", creator, TaskStatus.PENDING);

    String newTitle = "Renamed title";

    task.rename(newTitle);

    assertEquals(task.getTitle(), newTitle);
  }

  @Test
  @DisplayName("should throw illegal argument exception when try rename a finishedTask")
  void shouldThrowIllegalArgumentExceptionWhenTryRenameAFinishedTask() {
    User creator = new User();

    Task task = new Task("Title", "Description", creator, TaskStatus.FINISHED);

    assertThrows(IllegalArgumentException.class, () -> {
      task.rename(null);
    });
  }

  @Test
  @DisplayName("should throw task already finished exception when try rename a finishedTask")
  void shouldThrowTaskAlreadyFinishedExceptionWhenTryRenameAFinishedTask() {
    User creator = new User();

    Task task = new Task("Title", "Description", creator, TaskStatus.FINISHED);

    assertThrows(TaskAlreadyFinishedException.class, () -> {
      task.rename("RenameTask");
    });
  }

  @Test
  @DisplayName("should throw task already finished exception when try update description of finishedTask")
  void shouldThrowTaskAlreadyFinishedExceptionWhenTryUpdateDescriptionOfFinishedTask() {
    User creator = new User();

    Task task = new Task("Title", "Description", creator, TaskStatus.FINISHED);

    assertThrows(TaskAlreadyFinishedException.class, () -> {
      task.updateDescription("RenameTask");
    });
  }

  @Test
  @DisplayName("Should be able to add a subtask")
  void shouldBeAbleToAddASubtask() {
    User creator = new User();

    Task task = new Task("Title", "Description", creator, TaskStatus.PENDING);

    Task subTask = new Task("Subtask", "Description", creator, TaskStatus.PENDING);

    task.addSubtask(subTask);

    assertTrue(task.getSubtasks().contains(subTask));
    assertEquals(task, subTask.getParentTask());
  }

  @Test
  @DisplayName("Should not allow subtask to have its own subtask")
  void shouldNotAllowSubtaskToHaveItsOwnSubtask() {
    User creator = new User();

    Task parent = new Task("Parent", "Desc", creator, TaskStatus.PENDING);
    Task child = new Task("Child", "Desc", creator, TaskStatus.PENDING);

    parent.addSubtask(child);

    Task grandChild = new Task("GrandChild", "Desc", creator, TaskStatus.PENDING);

    assertThrows(SubtaskAlreadyHasParentException.class, () -> {
      child.addSubtask(grandChild);
    });
  }

  @Test
  @DisplayName("Should not add subtask to cancelled task")
  void shouldNotAddSubtaskToCancelledTask() {
    User creator = new User();

    Task cancelled = new Task("Task", "Desc", creator, TaskStatus.CANCELLED);
    Task subtask = new Task("Sub", "Desc", creator, TaskStatus.PENDING);

    assertThrows(CannotAddSubtaskToCancelledTaskException.class, () -> {
      cancelled.addSubtask(subtask);
    });
  }

  @Test
  @DisplayName("Should not allow null subtask")
  void shouldNotAllowNullSubtask() {
    User creator = new User();

    Task parent = new Task("Task", "Desc", creator, TaskStatus.PENDING);

    CannotBeNullException ex = assertThrows(CannotBeNullException.class, () -> {
      parent.addSubtask(null);
    });

    assertEquals(ex.getMessage(), "Subtask cannot be null!");
  }

  @Test
  @DisplayName("Should not allow subtask that already has parent")
  void shouldNotAllowSubtaskThatAlreadyHasParent() {
    User creator = new User();

    Task parent1 = new Task("Parent1", "Desc", creator, TaskStatus.PENDING);
    Task parent2 = new Task("Parent2", "Desc", creator, TaskStatus.PENDING);

    Task subtask = new Task("Sub", "Desc", creator, TaskStatus.PENDING);

    parent1.addSubtask(subtask);

    assertThrows(SubtaskAlreadyHasParentException.class, () -> {
      parent2.addSubtask(subtask);
    });
  }

  @Test
  @DisplayName("Should change status from FINISHED to IN_PROGRESS when adding subtask")
  void shouldChangeStatusWhenFinished() {
    User creator = new User();

    Task parent = new Task("Task", "Desc", creator, TaskStatus.FINISHED);
    Task subtask = new Task("Sub", "Desc", creator, TaskStatus.PENDING);

    parent.addSubtask(subtask);

    assertEquals(TaskStatus.IN_PROGRESS, parent.getStatus());
  }
}
