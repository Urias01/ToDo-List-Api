package com.todo.domain.task.exception;

public class MaxResponsiblesReachedException extends RuntimeException {

  public MaxResponsiblesReachedException() {
    super("A task cannot have more than 2 responsibles");
  }
}
