package com.todo.application.ports.user;

import java.util.Optional;
import java.util.UUID;

import com.todo.domain.user.entities.User;

public interface IUserQueryRepository {
  Optional<User> findById(UUID id);

  Optional<User> findByEmail(String email);
}
