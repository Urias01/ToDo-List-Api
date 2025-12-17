package com.todo.infrastructure.security.jwt;

public class JwtAuthContext {
  
  private static final ThreadLocal<JwtAuthContext> context = new ThreadLocal<>();

  private final String userId;

  public JwtAuthContext(String userId) {
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

  public String getUserId() {
    return userId;
  }
}
