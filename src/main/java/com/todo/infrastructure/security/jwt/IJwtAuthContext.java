package com.todo.infrastructure.security.jwt;

import java.util.UUID;

public interface IJwtAuthContext {
  UUID getUserId();
}
