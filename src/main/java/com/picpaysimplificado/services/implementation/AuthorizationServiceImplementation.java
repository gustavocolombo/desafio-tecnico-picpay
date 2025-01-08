package com.picpaysimplificado.services.implementation;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationServiceImplementation implements AuthorizationService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public boolean authorizeTransaction(User sender, BigDecimal amount) {
        ResponseEntity<Map> response= restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            String message = (String) response.getBody().get("status");
            return "success".equalsIgnoreCase(message);
        } else return false;
    }
}
