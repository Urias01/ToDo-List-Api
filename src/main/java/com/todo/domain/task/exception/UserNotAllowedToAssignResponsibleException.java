package com.todo.domain.task.exception;

public class UserNotAllowedToAssignResponsibleException extends RuntimeException {

  public UserNotAllowedToAssignResponsibleException() {
    super("Just creator can assign or remove a responsible");
  }

}
