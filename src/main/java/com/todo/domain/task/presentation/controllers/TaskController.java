package com.todo.domain.task.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.task.presentation.requests.ChangeStatusRequest;
import com.todo.domain.task.presentation.requests.SubTaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.task.presentation.requests.TaskRequestUpdate;
import com.todo.domain.task.presentation.responses.TaskResponse;
import com.todo.infrastructure.security.jwt.IJwtAuthContext;
import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.task.AddSubtask;
import com.todo.application.usecase.task.ChangeSubtaskStatus;
import com.todo.application.usecase.task.ChangeTaskStatus;
import com.todo.application.usecase.task.CreateTask;
import com.todo.application.usecase.task.DeleteSubtask;
import com.todo.application.usecase.task.DeleteTask;
import com.todo.application.usecase.task.ListTasks;
import com.todo.application.usecase.task.UpdateSubtask;
import com.todo.application.usecase.task.UpdateTask;
import com.todo.application.usecase.task.ViewTaskDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final CreateTask createTask;
  private final AddSubtask addSubtask;
  private final UpdateTask updateTask;
  private final UpdateSubtask updateSubtask;
  private final ChangeTaskStatus changeTaskStatus;
  private final ChangeSubtaskStatus changeSubtaskStatus;
  private final ViewTaskDetails viewTaskDetails;
  private final ListTasks listTasks;
  private final DeleteTask deleteTask;
  private final DeleteSubtask deleteSubtask;
  private final IJwtAuthContext jwtAuthContext;

  @PostMapping
  public ResponseEntity<ApiResponse<UUID>> createTask(@RequestBody TaskRequest request) {
    UUID taskId = createTask.execute(request, jwtAuthContext.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(taskId));
  }

  @PostMapping("/{id}/subtask")
  public ResponseEntity<ApiResponse<UUID>> addSubtask(@PathVariable UUID id, @RequestBody SubTaskRequest request) {
    UUID response = addSubtask.execute(request, id, jwtAuthContext.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
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
  public ResponseEntity<ApiResponse<UUID>> changeTaskStatus(@PathVariable UUID id,
      @RequestBody ChangeStatusRequest status) {
    UUID taskId = changeTaskStatus.execute(id, status, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }

  @PatchMapping("/{id}/subtask/{subtaskId}")
  public ResponseEntity<ApiResponse<UUID>> changeTaskStatus(@PathVariable UUID id, @PathVariable UUID subtaskId,
      @RequestBody ChangeStatusRequest request) {
    UUID taskId = changeSubtaskStatus.execute(id, subtaskId, request, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(taskId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<TaskResponse>> viewTaskDetails(@PathVariable UUID id) {
    TaskResponse response = viewTaskDetails.execute(id, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<TaskResponse>>> listTasks() {
    List<TaskResponse> response = listTasks.execute(jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteTask(@PathVariable UUID id) {
    deleteTask.execute(id, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{id}/subtask/{subtaskId}")
  public ResponseEntity<ApiResponse> deleteTask(@PathVariable UUID id, @PathVariable UUID subtaskId) {
    deleteSubtask.execute(id, subtaskId, jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
