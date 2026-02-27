package com.todo.exceptions;

public class UserNotAllowedToAssignResponsibleException extends RuntimeException {

  public UserNotAllowedToAssignResponsibleException() {
    super("Just creator can assign or remove a responsible");
  }

}
