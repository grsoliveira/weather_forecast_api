package com.olixcorp.weather_forecast_api.controller;

import java.util.Map;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
    String message = ex.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .findFirst()
        .orElse("Invalid parameter");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
  }

}
