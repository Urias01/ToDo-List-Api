package com.todo.mappers;

import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.request.UserRequest;
import com.todo.domain.user.presentation.response.UserResponse;

public class UserMapper {

  public static User toEntity(UserRequest request) {
    return new User(request.name(), request.email());
  }

  public static UserResponse toResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
