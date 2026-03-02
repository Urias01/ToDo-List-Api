package com.todo.domain.task.exception;

public class CannotAddSubtaskToCancelledTaskException extends RuntimeException {
  public CannotAddSubtaskToCancelledTaskException() {
    super("Cannot add subtasks to a cancelled task");
  }

}
