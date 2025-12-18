package com.todo.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.todo.exceptions.BadRequestException;
import com.todo.models.entities.User;
import com.todo.models.requests.UserRequest;
import com.todo.ports.user.IUserCommandRepository;
import com.todo.ports.user.IUserQueryRepository;
import com.todo.services.user.CreateUser;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

  @InjectMocks
  private CreateUser createUser;

  @Mock
  private IUserQueryRepository userQueryRepository;
  @Mock
  private IUserCommandRepository userCommandRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("should create a new user successfully")
  public void testCreateUserSuccess() {
    UserRequest request = new UserRequest("John Doe", "john@doe.com", "password123", "password123");

    when(userQueryRepository.findByEmail(request.email())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword");

    when(userCommandRepository.save(any(User.class))).thenAnswer(invocation -> {
      User savedUser = invocation.getArgument(0);
      savedUser.setId("user-1");
      return savedUser;
    });

    String userId = createUser.execute(request);

    assertEquals("user-1", userId);
  }

  @Test
  @DisplayName("should throw exception when email already exists")
  public void testCreateUserEmailExists() {
    UserRequest request = new UserRequest("John Doe", "john@doe.com", "password123", "password123");
    User existingUser = new User();
    existingUser.setEmail(request.email());

    when(userQueryRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

    assertThrows(com.todo.exceptions.AlreadyExistsException.class, () -> {
      createUser.execute(request);
    });
  }

  @Test
  @DisplayName("should throw exception when passwords do not match")
  public void testCreateUserPasswordsDoNotMatch() {
    UserRequest request = new UserRequest("John Doe", "john@doe.com", "password123", "wrongConfirm");

    when(userQueryRepository.findByEmail(request.email())).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> {
      createUser.execute(request);
    });
  }
}
