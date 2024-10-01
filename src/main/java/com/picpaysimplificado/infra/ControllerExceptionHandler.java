package com.picpaysimplificado.infra;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.picpaysimplificado.dtos.error.ExceptionDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {
  
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity treatDuplicateEntry(DataIntegrityViolationException e) {
    ExceptionDTO responseError = new ExceptionDTO("User already registered", 400);
    return ResponseEntity.badRequest().body(responseError);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity treatEntityNotFound(EntityNotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity treatGeneralException(Exception e) {
    ExceptionDTO responseError = new ExceptionDTO(e.getMessage(), 500);
    return ResponseEntity.internalServerError().body(responseError);
  }

  @ExceptionHandler(HttpServerErrorException.class)
  public ResponseEntity treatBadGateway(HttpServerErrorException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Notification service if offline");
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity treatHttpClientError(HttpClientErrorException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Transaction not allowed by web service");
  }
}
