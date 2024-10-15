package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;

public interface TokenService {
  String generateToken(User user);
  String validateToken(String token);
}
