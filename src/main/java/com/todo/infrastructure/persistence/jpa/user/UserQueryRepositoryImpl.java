package com.todo.infrastructure.persistence.jpa.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.todo.domain.user.entities.User;
import com.todo.application.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements IUserQueryRepository {

  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
