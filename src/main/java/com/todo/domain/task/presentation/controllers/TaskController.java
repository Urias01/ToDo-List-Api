package com.todo.domain.task.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.infrastructure.security.jwt.IJwtAuthContext;
import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.task.ChangeSubtaskStatus;
import com.todo.application.usecase.task.ChangeTaskStatus;
import com.todo.application.usecase.task.CreateTask;
import com.todo.application.usecase.task.UpdateSubtask;
import com.todo.application.usecase.task.UpdateTask;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final CreateTask createTask;
  private final UpdateTask updateTask;
  private final UpdateSubtask updateSubtask;
  private final ChangeTaskStatus changeTaskStatus;
  private final ChangeSubtaskStatus changeSubtaskStatus;
  private final IJwtAuthContext jwtAuthContext;

  @PostMapping
  public ResponseEntity<ApiResponse<UUID>> createTask(@RequestBody TaskRequest request) {
    UUID taskId = createTask.execute(request, jwtAuthContext.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(taskId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<UUID>> updateTask(@PathVariable UUID id,
      @RequestBody TaskRequestUpdate request) {
    UUID taskId = updateTask.execute(request, id, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }

  @PutMapping("/{id}/subtask/{subtaskId}")
  public ResponseEntity<ApiResponse<UUID>> updateSubtask(@PathVariable UUID id, @PathVariable UUID subtaskId,
      @RequestBody TaskRequestUpdate request) {
    UUID taskId = updateSubtask.execute(id, subtaskId, request, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<UUID>> changeTaskStatus(@PathVariable UUID id, @RequestBody TaskStatus request) {
    UUID taskId = changeTaskStatus.execute(id, request, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }

  @PatchMapping("/{id}/subtask/{subtaskId}")
  public ResponseEntity<ApiResponse<UUID>> changeTaskStatus(@PathVariable UUID id, @PathVariable UUID subtaskId,
      @RequestBody TaskStatus request) {
    UUID taskId = changeSubtaskStatus.execute(id, subtaskId, request, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }
}
