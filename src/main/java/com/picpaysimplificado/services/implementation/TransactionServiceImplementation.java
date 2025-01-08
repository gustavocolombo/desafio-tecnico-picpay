package com.picpaysimplificado.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.transaction.TransactionEntityDTO;
import com.picpaysimplificado.dtos.transaction.TransactionRequestDTO;
import com.picpaysimplificado.dtos.transaction.ValidateTransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import com.picpaysimplificado.services.TransactionService;

@Service
public class TransactionServiceImplementation implements TransactionService{
  @Autowired
  private UserServiceImplementation userService;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private NotificationServiceImplementation notificationService;

  @Autowired
  private AuthorizationServiceImplementation authorizationService;

  @Override
  @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Transaction createTransaction(TransactionRequestDTO transaction) throws Exception{
    User sender = this.userService.findById(transaction.senderId());
    User receiver =  this.userService.findById(transaction.receiverId());

    ValidateTransactionDTO validateTransaction = new ValidateTransactionDTO(sender, receiver, transaction.amount());
    this.userService.validateTransaction(validateTransaction);

    boolean isAuthorized = this.authorizationService.authorizeTransaction(sender, transaction.amount());
    if(!isAuthorized) throw new Exception("Transaction not allowed");

    TransactionEntityDTO newTransactionEntity = new TransactionEntityDTO(transaction.amount(), sender, receiver);
    Transaction newTransaction = new Transaction(newTransactionEntity);

    sender.setBalance(sender.getBalance().subtract(transaction.amount()));
    receiver.setBalance(receiver.getBalance().add(transaction.amount()));
    
    this.transactionRepository.save(newTransaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);

    this.notificationService.sendNotification(sender, "Transaction sended successfully");
    this.notificationService.sendNotification(receiver, "Transaction received successfully");

    return newTransaction;
  }
}
