package com.picpaysimplificado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.user.LoginRequestDTO;
import com.picpaysimplificado.services.implementation.TokenServiceImplementation;
import com.picpaysimplificado.services.implementation.UserServiceImplementation;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/auth")
public class AuthController {
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserServiceImplementation userService;

  @Autowired
  private TokenServiceImplementation tokenService;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginRequestDTO loginRequestDTO) {
    User user = this.userService.findByEmail(loginRequestDTO.email());

    if(user == null) throw new EntityNotFoundException("User not found");

    if(passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
      String token = this.tokenService.generateToken(user);
      return ResponseEntity.ok(token);
    }

    return ResponseEntity.badRequest().build();
  }

}
