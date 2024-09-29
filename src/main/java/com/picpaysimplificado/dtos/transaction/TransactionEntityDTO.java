package com.picpaysimplificado.dtos.transaction;

import java.math.BigDecimal;

import com.picpaysimplificado.domain.user.User;

public record TransactionEntityDTO(BigDecimal amount, User sender, User receiver) {
  
}
