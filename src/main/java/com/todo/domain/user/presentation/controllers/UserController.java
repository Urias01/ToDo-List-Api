package com.todo.domain.user.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.application.usecase.user.CreateUser;
import com.todo.domain.user.presentation.request.UserRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final CreateUser createUser;

  @PostMapping()
  public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserRequest request) {
    String userId = createUser.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userId));
  }

}