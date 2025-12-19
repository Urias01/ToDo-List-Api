package com.todo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.models.requests.TaskRequest;
import com.todo.models.responses.ApiResponse;
import com.todo.services.task.CreateTask;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final CreateTask createTask;

  @PostMapping
  public ResponseEntity<ApiResponse<String>> createTask(@RequestBody TaskRequest request) {
    String taskId = createTask.execute(request);
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }
}
