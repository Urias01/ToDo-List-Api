package com.todo.infrastructure.persistence.jpa.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.todo.models.entities.User;
import com.todo.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements IUserQueryRepository {
  
  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
