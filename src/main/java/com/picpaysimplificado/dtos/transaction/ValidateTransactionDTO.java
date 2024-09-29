package com.picpaysimplificado.dtos.transaction;

import java.math.BigDecimal;

import com.picpaysimplificado.domain.user.User;

public record ValidateTransactionDTO(User sender, User receiver, BigDecimal amount) {
  
}
