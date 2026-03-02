package com.todo.application.ports.user;

import com.todo.domain.user.entities.User;

public interface IUserCommandRepository {
  User save(User user);
}
