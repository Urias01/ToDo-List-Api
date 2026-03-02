package com.todo.mappers;

import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.request.UserRequest;

public class UserMapper {

  public static User toEntity(UserRequest request) {
    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email());
    user.setPassword(request.password());
    return user;
  }

}
