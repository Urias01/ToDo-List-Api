package com.todo.infrastructure.security.jwt;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.todo.config.AppProperties;

@Service
public class JwtService {
  
  private final AppProperties appProperties;
  private final String secret;

  public JwtService(AppProperties appProperties) {
    this.appProperties = appProperties;
    this.secret = appProperties.getJwtSecret();
  }

  public String extractUserId(String token) {
    return JWT.require(Algorithm.HMAC256(secret))
        .withIssuer(appProperties.getJwtIssuer())
        .build()
        .verify(token)
        .getSubject();
  }

  public boolean isTokenValid(String token) {
    try {
      JWT.require(Algorithm.HMAC256(appProperties.getJwtSecret()))
          .withIssuer(appProperties.getJwtIssuer())
          .build()
          .verify(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String generateToken(String userId) {
    return JWT.create()
        .withIssuer(appProperties.getJwtIssuer())
        .withSubject(userId)
        .sign(Algorithm.HMAC256(secret));
  }
}
