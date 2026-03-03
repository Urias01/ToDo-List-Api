package com.todo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.todo.domain.task.exception.CannotAddSubtaskToCancelledTaskException;
import com.todo.domain.task.exception.CannotBeNullException;
import com.todo.domain.task.exception.MaxResponsiblesReachedException;
import com.todo.domain.task.exception.SubtaskAlreadyHasParentException;
import com.todo.domain.task.exception.TaskAlreadyFinishedException;
import com.todo.domain.task.exception.UserNotAllowedToPerformActionInTask;
import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.common.exceptions.BadRequestException;
import com.todo.application.exceptions.AlreadyExistsException;
import com.todo.domain.user.expections.AuthenticationException;

@RestControllerAdvice
public class GlobalException {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<ApiResponse<Void>> handleAlreadyExistsException(AlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(CannotBeNullException.class)
  public ResponseEntity<ApiResponse<Void>> handleCannotBeNullException(CannotBeNullException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(SubtaskAlreadyHasParentException.class)
  public ResponseEntity<ApiResponse<Void>> handleSubtaskAlreadyHasParentException(SubtaskAlreadyHasParentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(TaskAlreadyFinishedException.class)
  public ResponseEntity<ApiResponse<Void>> handleTaskAlreadyFinishedException(TaskAlreadyFinishedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(CannotAddSubtaskToCancelledTaskException.class)
  public ResponseEntity<ApiResponse<Void>> handleCannotAddSubtaskToCancelledTaskException(
      CannotAddSubtaskToCancelledTaskException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(MaxResponsiblesReachedException.class)
  public ResponseEntity<ApiResponse<Void>> handleMaxResponsiblesReachedException(
      MaxResponsiblesReachedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage()));
  }

  @ExceptionHandler(UserNotAllowedToPerformActionInTask.class)
  public ResponseEntity<ApiResponse<Void>> handleUserNotAllowedToPerformActionInTask(
      UserNotAllowedToPerformActionInTask ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(ex.getMessage()));
  }
}
