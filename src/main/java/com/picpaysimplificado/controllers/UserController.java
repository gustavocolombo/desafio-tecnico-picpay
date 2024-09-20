package com.picpaysimplificado.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.CreateUserDTO;
import com.picpaysimplificado.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping("/")
  public ResponseEntity createUser(@RequestBody CreateUserDTO createUserDTO) {
     try {
      User user = this.userService.createUser(createUserDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(user);
     } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     }
  }
}
