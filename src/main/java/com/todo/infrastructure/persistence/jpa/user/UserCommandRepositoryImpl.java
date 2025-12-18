package com.todo.infrastructure.persistence.jpa.user;

import org.springframework.stereotype.Repository;

import com.todo.models.entities.User;
import com.todo.ports.user.IUserCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCommandRepositoryImpl implements IUserCommandRepository {
  
  private final UserRepository userRepository;

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }
}
