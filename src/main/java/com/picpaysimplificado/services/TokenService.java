package com.picpaysimplificado.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.picpaysimplificado.domain.user.User;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create()
          .withIssuer("login-auth")
          .withSubject(user.getEmail())
          .withExpiresAt(this.generateExpirationTokenTime())
          .sign(algorithm);
    } catch (JWTCreationException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public String validateToken(String token){
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
          .withIssuer("login-auth")
          .build()
          .verify(token)
          .getSubject();
    } catch(JWTVerificationException e) {
      return null;
    }
  }

  private Instant generateExpirationTokenTime() {
    return LocalDateTime.now().plusHours(2).atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
  }
}
