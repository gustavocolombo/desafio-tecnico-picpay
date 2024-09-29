package com.picpaysimplificado.dtos.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(BigDecimal amount, UUID senderId, UUID receiverId) {
  
}
