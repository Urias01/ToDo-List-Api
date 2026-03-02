package com.todo.domain.user.expections;

public class AuthenticationException extends RuntimeException {

  public AuthenticationException(String message) {
    super(message + " authentication failed.");
  }

}
