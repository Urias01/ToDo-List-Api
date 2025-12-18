package com.todo.models.requests;

public record UserRequest(String name, String email, String password, String confirmPassword) {
  
}
