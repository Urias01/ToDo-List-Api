package com.todo.domain.common.exceptions;

public class AuthenticationException extends RuntimeException {

  public AuthenticationException(String message) {
    super(message + " authentication failed.");
  }

}
