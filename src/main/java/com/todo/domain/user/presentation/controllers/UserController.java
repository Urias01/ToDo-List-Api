package com.todo.domain.user.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.user.CreateUser;
import com.todo.application.usecase.user.GetCurrentUser;
import com.todo.domain.user.presentation.request.UserRequest;
import com.todo.domain.user.presentation.response.UserResponse;
import com.todo.infrastructure.security.jwt.IJwtAuthContext;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final CreateUser createUser;
  private final GetCurrentUser getCurrentUser;
  private final IJwtAuthContext jwtAuthContext;

  @PostMapping()
  public ResponseEntity<ApiResponse<UUID>> createUser(@RequestBody UserRequest request) {
    UUID userId = createUser.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userId));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
    UserResponse response = getCurrentUser.execute(jwtAuthContext.getUserId());
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}