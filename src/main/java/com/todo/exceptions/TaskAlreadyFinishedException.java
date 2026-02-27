package com.todo.exceptions;

public class TaskAlreadyFinishedException extends RuntimeException {

  public TaskAlreadyFinishedException() {
    super("Task already finished");
  }

}
