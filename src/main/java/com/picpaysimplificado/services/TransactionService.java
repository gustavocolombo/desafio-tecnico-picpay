package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;

@Service
public class TransactionService {
  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private RestTemplate restTemplate;
  
  public void createTransaction(TransactionDTO transaction) throws Exception{
    User sender = this.userService.findById(transaction.senderId());
    User receiver =  this.userService.findById(transaction.receiverId());

    this.userService.validateTransaction(sender, transaction.amount());

    boolean isAuthorized = this.authorizeTransaction(sender, transaction.amount());

    if(!isAuthorized) throw new Exception("Transaction not allowed");

    Transaction newTransaction = new Transaction();
    newTransaction.setSender(sender);
    newTransaction.setReceiver(receiver);
    newTransaction.setAmount(transaction.amount());

    sender.setBalance(sender.getBalance().subtract(transaction.amount()));
    receiver.setBalance(receiver.getBalance().add(transaction.amount()));
    
    this.transactionRepository.save(newTransaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);
  }

  public boolean authorizeTransaction(User sender, BigDecimal amount) {
    ResponseEntity<Map> response= restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
    
    if(response.getStatusCode() == HttpStatus.OK) {
      String message = (String) response.getBody().get("message");
      return "Autorizado".equalsIgnoreCase(message);
    } else return false;
  }
}
