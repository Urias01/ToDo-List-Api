package com.todo.application.usecase.user;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.request.UserRequest;
import com.todo.application.exceptions.AlreadyExistsException;
import com.todo.domain.common.exceptions.BadRequestException;
import com.todo.mappers.UserMapper;
import com.todo.application.ports.user.IUserCommandRepository;
import com.todo.application.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUser {

  private final PasswordEncoder passwordEncoder;

  private final IUserCommandRepository userCommandRepository;
  private final IUserQueryRepository userQueryRepository;

  public UUID execute(UserRequest request) {

    User user = userQueryRepository.findByEmail(request.email()).orElse(null);

    if (user != null) {
      throw new AlreadyExistsException("User with email already exists");
    }

    if (!request.password().equals(request.confirmPassword())) {
      throw new BadRequestException("Password and confirm password do not match");
    }

    User newUser = UserMapper.toEntity(request);
    String hashedPassword = passwordEncoder.encode(request.password());
    newUser.changePassword(hashedPassword);

    newUser = userCommandRepository.save(newUser);

    return newUser.getId();
  }
}
