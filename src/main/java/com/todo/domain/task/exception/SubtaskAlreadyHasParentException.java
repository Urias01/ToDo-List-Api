package com.todo.domain.task.exception;

public class SubtaskAlreadyHasParentException extends RuntimeException {

  public SubtaskAlreadyHasParentException() {
    super("Subtasks cannot have their own subtasks");
  }

}
