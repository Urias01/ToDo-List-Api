package com.todo.infrastructure.security.jwt;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthContextAdapter implements IJwtAuthContext {

    @Override
    public UUID getUserId() {
        JwtAuthContext ctx = JwtAuthContext.get();
        return ctx != null ? ctx.getUserId() : null;
    }

}
