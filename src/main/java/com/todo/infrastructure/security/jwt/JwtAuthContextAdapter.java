package com.todo.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthContextAdapter implements IJwtAuthContext {

    @Override
    public String getUserId() {
        JwtAuthContext ctx = JwtAuthContext.get();
        return ctx != null ? ctx.getUserId() : null;
    }

}

