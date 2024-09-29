package com.picpaysimplificado.dtos.user;

import java.math.BigDecimal;

import com.picpaysimplificado.domain.user.UserType;

public record CreateUserDTO(String firstname, String lastname, String email, String password, String document, UserType userType, BigDecimal balance) {
  
}
