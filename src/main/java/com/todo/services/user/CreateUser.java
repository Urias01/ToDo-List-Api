package com.todo.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.exceptions.AlreadyExistsException;
import com.todo.exceptions.BadRequestException;
import com.todo.mappers.UserMapper;
import com.todo.models.entities.User;
import com.todo.models.requests.UserRequest;
import com.todo.ports.user.IUserCommandRepository;
import com.todo.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUser {
  
  
  private final PasswordEncoder passwordEncoder;

  private final IUserCommandRepository userCommandRepository;
  private final IUserQueryRepository userQueryRepository;

  public String execute(UserRequest request) {
    
    User user = userQueryRepository.findByEmail(request.email()).orElse(null);

    if (user != null) {
      throw new AlreadyExistsException("User with email already exists");
    }

    if (!request.password().equals(request.confirmPassword())) {
      throw new BadRequestException("Password and confirm password do not match");
    }

    user = UserMapper.toEntity(request);
    String hashedPassword = passwordEncoder.encode(request.password());
    user.setPassword(hashedPassword);

    userCommandRepository.save(user);

    return user.getId();
  }
}
