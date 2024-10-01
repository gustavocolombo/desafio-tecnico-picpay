package com.picpaysimplificado.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.transaction.ValidateTransactionDTO;
import com.picpaysimplificado.dtos.user.CreateUserDTO;
import com.picpaysimplificado.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void validateTransaction(ValidateTransactionDTO validateTransactionDTO) throws Exception{
    if(validateTransactionDTO.sender().getUserType().equals(UserType.MERCHANT)) {
      throw new Exception("User not allowed for this operation");
    }

    if(validateTransactionDTO.sender().getId().equals(validateTransactionDTO.receiver().getId())) {
      throw new Exception("Receiver can not be a sender user");
    }

    if(validateTransactionDTO.sender().getBalance().compareTo(validateTransactionDTO.amount()) < 0) {
      throw new Exception("Amount is higher than actual balance of sender, pick a smaller value");
    }
  }
  
  public User findById(UUID userId) throws Exception {
     User user = this.userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

     return user;
  }

  public User findByEmail(String email) {
    User user = this.userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with this email"));

    return user;
  }

  public User saveUser(User user) {
    User savedUser = this.userRepository.save(user);

    return savedUser;
  }

  public User createUser(CreateUserDTO createUserDTO) throws Exception{
    Optional<User> verifyUserExistsWithDocument = this.userRepository.findByDocument(createUserDTO.document());

    Optional<User> verifyUserExistsWithEmail = this.userRepository.findByEmail(createUserDTO.email());

    if(!(verifyUserExistsWithDocument.isEmpty())) {
      throw new Exception("User with this document already exists");
    }

    if(!(verifyUserExistsWithEmail.isEmpty())) {
      throw new Exception("User with this email already exists");
    }

    User newUser = new User(createUserDTO);

    String passEncoded = passwordEncoder.encode(createUserDTO.password());
    newUser.setPassword(passEncoded);

    return this.userRepository.save(newUser);
  }
} 
