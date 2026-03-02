package com.todo.domain.task.enums;

public enum TaskStatus {
  PENDING("PENDING"),
  IN_PROGRESS("IN_PROGRESS"),
  FINISHED("FINISHED"),
  CANCELLED("CANCELLED");

  private String status;

  TaskStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
