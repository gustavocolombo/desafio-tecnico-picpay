package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.user.CreateUserDTO;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepository;

  public void validateTransaction(User sender, BigDecimal amount) throws Exception{
    if(sender.getUserType().equals(UserType.MERCHANT)) {
      throw new Exception("User not allowed for this operation");
    }

    if(sender.getBalance().compareTo(amount) < 0) {
      throw new Exception("Amount is higher than actual balance of sender, pick a smaller value");
    }
  }
  
  public User findById(UUID userId) throws Exception{
     User user = this.userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

     return user;
  }

  public User saveUser(User user) {
    User savedUser = this.userRepository.save(user);

    return savedUser;
  }

  public User createUser(CreateUserDTO createUserDTO) throws Exception{
    Optional<User> verifyUserExistsWithDocument = this.userRepository.findByDocument(createUserDTO.document());

    Optional<User> verifyUserExistsWithEmail = this.userRepository.findByEmail(createUserDTO.email());

    System.out.println(verifyUserExistsWithDocument.isEmpty() == true);

    if(!(verifyUserExistsWithDocument.isEmpty())) {
      throw new Exception("User with this document already exists");
    }

    if(!(verifyUserExistsWithEmail.isEmpty())) {
      throw new Exception("User with this email already exists");
    }

    User newUser = new User(createUserDTO);

    return this.userRepository.save(newUser);
  }
} 
