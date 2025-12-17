package com.todo.ports.user;

import java.util.Optional;

import com.todo.models.entities.User;

public interface IUserQueryRepository {
  Optional<User> findById(String id);
  Optional<User> findByEmail(String email);
}
