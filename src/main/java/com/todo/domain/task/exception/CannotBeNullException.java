package com.todo.domain.task.exception;

public class CannotBeNullException extends RuntimeException {

  public CannotBeNullException(String message) {
    super(message + " cannot be null!");
  }

}
