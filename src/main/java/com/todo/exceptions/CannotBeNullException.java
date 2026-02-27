package com.todo.exceptions;

public class CannotBeNullException extends RuntimeException {

  public CannotBeNullException(String message) {
    super(message + " cannot be null!");
  }

}
