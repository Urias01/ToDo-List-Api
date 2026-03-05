package com.todo.mappers;

import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.request.UserRequest;

public class UserMapper {

  public static User toEntity(UserRequest request) {
    return new User(request.name(), request.email());
  }

}
