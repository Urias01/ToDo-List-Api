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

import com.todo.exceptions.AuthenticationException;
import com.todo.infrastructure.security.jwt.JwtService;
import com.todo.models.entities.User;
import com.todo.models.requests.AuthRequest;
import com.todo.models.responses.AuthResponse;
import com.todo.ports.user.IUserQueryRepository;
import com.todo.services.auth.SignIn;

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
    User user = new User();
    user.setId("userId");
    user.setEmail("email@test.com");
    user.setPassword("encodedPassword");

    AuthRequest request = new AuthRequest("email@test.com", "password");

    when(userQueryRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
    when(jwtService.generateToken("userId")).thenReturn("token");

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
    user.setId("userId");
    user.setEmail("email@test.com");
    user.setPassword("encodedPassword");

    when(userQueryRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("invalidPassword", "encodedPassword")).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> signIn.execute(request));
  }

}
