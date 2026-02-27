package com.todo.exceptions;

public class SubtaskAlreadyHasParentException extends RuntimeException {

  public SubtaskAlreadyHasParentException() {
    super("Subtasks cannot have their own subtasks");
  }

}
