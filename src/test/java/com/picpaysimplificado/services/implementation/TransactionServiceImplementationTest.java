package com.picpaysimplificado.services.implementation;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.transaction.TransactionRequestDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplementationTest {

    @Mock
    private UserServiceImplementation userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationServiceImplementation notificationService;

    @Mock
    private AuthorizationServiceImplementation authorizationService;

    @Autowired
    @InjectMocks
    private TransactionServiceImplementation transactionService;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("it should be able to create a transaction succesfully when transaction is allowed")
    void createTransactionWithSucess() throws Exception {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();

        User sender = new User(
                firstId,
                "John",
                "Doe",
                "johndoe@gmail.com",
                "password",
                "99999999901",
                new BigDecimal(10),
                UserType.COMMON,
                 null
        );

        User receiver = new User(
                secondId,
                "Jane",
                "Doe",
                "janedow@gmail.com",
                "password1",
                "99999999902",
                new BigDecimal(20),
                UserType.COMMON,
                null
        );

        when(userService.findById(firstId)).thenReturn(sender);
        when(userService.findById(secondId)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionRequestDTO request = new TransactionRequestDTO(new BigDecimal(10),  firstId, secondId);
        this.transactionService.createTransaction(request);

        verify(transactionRepository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(30));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transaction sended successfully");
        verify(notificationService, times(1)).sendNotification(receiver, "Transaction received successfully");
    }

    @Test
    @DisplayName("it should not be able to create a transaction when transaction is not allowed")
    void createTransactionWithError() throws Exception {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();

        User sender = new User(
                firstId,
                "John",
                "Doe",
                "johndoe@gmail.com",
                "password",
                "99999999901",
                new BigDecimal(10),
                UserType.COMMON,
                null
        );

        User receiver = new User(
                secondId,
                "Jane",
                "Doe",
                "janedow@gmail.com",
                "password1",
                "99999999902",
                new BigDecimal(20),
                UserType.COMMON,
                null
        );

        when(userService.findById(firstId)).thenReturn(sender);
        when(userService.findById(secondId)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction(sender, new BigDecimal(10))).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionRequestDTO request = new TransactionRequestDTO(new BigDecimal(10),  firstId, secondId);
            this.transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transaction not allowed", thrown.getMessage());
    }
}