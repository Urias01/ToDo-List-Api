package com.todo.services.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.exceptions.AuthenticationException;
import com.todo.exceptions.NotFoundException;
import com.todo.infrastructure.security.jwt.JwtService;
import com.todo.models.entities.User;
import com.todo.models.requests.AuthRequest;
import com.todo.models.responses.AuthResponse;
import com.todo.ports.user.IUserQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignIn {

  private final IUserQueryRepository userQueryRepository;
  
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse execute(AuthRequest request) {
    User user = userQueryRepository.findByEmail(request.email())
        .orElseThrow(() -> new AuthenticationException(""));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new AuthenticationException("Invalid email or password");
    }

    String token = jwtService.generateToken(user.getId().toString());

    return new AuthResponse(token);
  }
}
