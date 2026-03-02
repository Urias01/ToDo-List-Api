package com.todo.domain.task.exception;

public class TaskAlreadyFinishedException extends RuntimeException {

  public TaskAlreadyFinishedException() {
    super("Task already finished");
  }

}
