package com.todo.exceptions;

public class CannotAddSubtaskToCancelledTaskException extends RuntimeException {
  public CannotAddSubtaskToCancelledTaskException() {
    super("Cannot add subtasks to a cancelled task");
  }

}
