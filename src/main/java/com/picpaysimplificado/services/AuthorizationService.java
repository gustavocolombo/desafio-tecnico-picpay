package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;

public interface AuthorizationService {
    boolean authorizeTransaction(User sender, BigDecimal amount);
}
