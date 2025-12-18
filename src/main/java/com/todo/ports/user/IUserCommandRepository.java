package com.todo.ports.user;

import com.todo.models.entities.User;

public interface IUserCommandRepository {
  User save(User user);
}
