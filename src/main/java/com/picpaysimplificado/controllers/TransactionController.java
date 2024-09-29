package com.picpaysimplificado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.dtos.transaction.TransactionRequestDTO;
import com.picpaysimplificado.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @PostMapping("/")
  public ResponseEntity createTransaction(@RequestBody TransactionRequestDTO transactioDTO) throws Exception{
      Transaction transaction = this.transactionService.createTransaction(transactioDTO);

      return ResponseEntity.status(HttpStatus.OK).body(transaction);
  }
}
