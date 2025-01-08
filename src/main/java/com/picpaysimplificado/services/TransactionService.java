package com.picpaysimplificado.services;

import java.math.BigDecimal;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.transaction.TransactionRequestDTO;

public interface TransactionService {
  Transaction createTransaction(TransactionRequestDTO transactionRequestDTO) throws Exception;
}
