package com.todo.domain.task.exception;

public class SubtaskIsNotPresentInTaskException extends RuntimeException {

  public SubtaskIsNotPresentInTaskException() {
    super("The subtask is not present in the task");
  }
}
