package com.todo.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.todo.domain.user.entities.User;
import com.todo.domain.user.presentation.request.AuthRequest;
import com.todo.domain.user.expections.AuthenticationException;
import com.todo.infrastructure.security.jwt.JwtService;
import com.todo.domain.user.presentation.response.AuthResponse;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.auth.SignIn;

@ExtendWith(MockitoExtension.class)
public class SignInTest {

  @InjectMocks
  private SignIn signIn;

  @Mock
  private IUserQueryRepository userQueryRepository;
  @Mock
  private JwtService jwtService;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should sign in user successfully")
  public void shouldSignInUserSuccessfully() {
    User user = new User("user", "user@mail.com");
    user.changePassword("encodedPassword");

    AuthRequest request = new AuthRequest("user@mail.com", "password");

    when(userQueryRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
    when(jwtService.generateToken(user.getId())).thenReturn("token");

    AuthResponse response = signIn.execute(request);
    assert response != null;
    assert response.token().equals("token");
  }

  @Test
  @DisplayName("Should throw exception for invalid email")
  public void shouldThrowExceptionForInvalidEmail() {
    AuthRequest request = new AuthRequest("invalid@test.com", "password");

    when(userQueryRepository.findByEmail("invalid@test.com")).thenReturn(Optional.empty());

    assertThrows(AuthenticationException.class, () -> signIn.execute(request));
  }

  @Test
  @DisplayName("Should throw exception for invalid password")
  public void shouldThrowExceptionForInvalidPassword() {
    AuthRequest request = new AuthRequest("email@test.com", "invalidPassword");

    User user = new User();
    user.changeEmail("email@test.com");
    user.changePassword("encodedPassword");

    when(userQueryRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("invalidPassword", "encodedPassword")).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> signIn.execute(request));
  }

}
