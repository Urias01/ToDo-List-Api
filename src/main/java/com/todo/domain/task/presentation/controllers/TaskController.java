package com.todo.domain.task.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.infrastructure.security.jwt.IJwtAuthContext;
import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.task.CreateTask;
import com.todo.application.usecase.task.UpdateTask;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final CreateTask createTask;
  private final UpdateTask updateTask;
  private final IJwtAuthContext jwtAuthContext;

  @PostMapping
  public ResponseEntity<ApiResponse<String>> createTask(@RequestBody TaskRequest request) {
    String taskId = createTask.execute(request, jwtAuthContext.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(taskId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> updateTask(@PathVariable String id,
      @RequestBody TaskRequestUpdate request) {
    String taskId = updateTask.execute(request, id, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }
}
