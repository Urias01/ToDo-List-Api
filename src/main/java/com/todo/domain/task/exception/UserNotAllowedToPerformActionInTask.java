package com.todo.domain.task.exception;

public class UserNotAllowedToPerformActionInTask extends RuntimeException {

  public UserNotAllowedToPerformActionInTask() {
    super("The user is not allowed to perform these actions on the task");
  }
}
