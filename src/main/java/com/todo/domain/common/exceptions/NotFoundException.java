package com.todo.domain.common.exceptions;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message + " not found.");
  }

}
