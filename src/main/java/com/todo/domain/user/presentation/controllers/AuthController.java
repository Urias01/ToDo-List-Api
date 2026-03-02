package com.todo.domain.user.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.domain.common.presentation.response.ApiResponse;
import com.todo.domain.user.presentation.response.AuthResponse;
import com.todo.application.usecase.auth.SignIn;
import com.todo.domain.user.presentation.request.AuthRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final SignIn signIn;

  @PostMapping("/sign-in")
  public ResponseEntity<ApiResponse<AuthResponse>> signIn(@RequestBody AuthRequest request) {
    AuthResponse auth = signIn.execute(request);
    return ResponseEntity.ok(ApiResponse.success(auth));
  }

}
