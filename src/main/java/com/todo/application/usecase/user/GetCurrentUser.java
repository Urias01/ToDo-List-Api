package com.todo.application.usecase.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.response.UserResponse;
import com.todo.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCurrentUser {

  private final IUserQueryRepository userQueryRepository;

  public UserResponse execute(UUID userId) {

    User currentUser = userQueryRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User"));

    return UserMapper.toResponse(currentUser);
  }
}
