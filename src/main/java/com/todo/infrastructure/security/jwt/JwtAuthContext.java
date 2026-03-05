package com.todo.infrastructure.security.jwt;

import java.util.UUID;

public class JwtAuthContext {

  private static final ThreadLocal<JwtAuthContext> context = new ThreadLocal<>();

  private final UUID userId;

  public JwtAuthContext(UUID userId) {
    this.userId = userId;
  }

  public static void set(JwtAuthContext authContext) {
    context.set(authContext);
  }

  public static JwtAuthContext get() {
    return context.get();
  }

  public static void clear() {
    context.remove();
  }

  public UUID getUserId() {
    return userId;
  }
}
