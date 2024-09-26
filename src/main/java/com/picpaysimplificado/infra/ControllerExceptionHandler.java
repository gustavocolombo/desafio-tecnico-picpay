package com.picpaysimplificado.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
