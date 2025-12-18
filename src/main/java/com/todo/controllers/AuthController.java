package com.todo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.models.requests.AuthRequest;
import com.todo.models.responses.ApiResponse;
import com.todo.models.responses.AuthResponse;
import com.todo.services.auth.SignIn;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final SignIn signIn;

  @PostMapping("/sign-in")
  public ResponseEntity<ApiResponse<AuthResponse>> signIn(@RequestBody AuthRequest request) {
    AuthResponse auth =  signIn.execute(request);
    return ResponseEntity.ok(ApiResponse.success(auth));
  }

}
