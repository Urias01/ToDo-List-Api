package com.todo.domain.task.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.infrastructure.security.jwt.IJwtAuthContext;
import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.task.CreateTask;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final CreateTask createTask;
  private final IJwtAuthContext jwtAuthContext;

  @PostMapping
  public ResponseEntity<ApiResponse<String>> createTask(@RequestBody TaskRequest request) {
    String taskId = createTask.execute(request, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }
}
