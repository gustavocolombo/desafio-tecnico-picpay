package com.picpaysimplificado.services;

import java.util.UUID;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.transaction.ValidateTransactionDTO;
import com.picpaysimplificado.dtos.user.CreateUserDTO;

public interface UserService {
  void validateTransaction(ValidateTransactionDTO validateTransactionDTO) throws Exception;
  User findById(UUID userId) throws Exception;
  User findByEmail(String email) throws Exception;
  User saveUser(User user) throws Exception;
  User createUser(CreateUserDTO createUserDTO) throws Exception;
}
