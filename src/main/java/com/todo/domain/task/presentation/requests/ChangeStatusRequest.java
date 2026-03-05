package com.todo.domain.task.presentation.requests;

import com.todo.domain.task.enums.TaskStatus;

public record ChangeStatusRequest(TaskStatus status) {

}
