package com.todo.application.ports.user;

import java.util.Optional;

import com.todo.domain.user.entities.User;

public interface IUserQueryRepository {
  Optional<User> findById(String id);

  Optional<User> findByEmail(String email);
}
