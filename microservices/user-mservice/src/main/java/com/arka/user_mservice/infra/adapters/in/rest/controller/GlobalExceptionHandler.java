package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.domain.exceptions.ResourceAlreadyExistsException;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(ResourceNotFoundException ex, ServerHttpRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .error("Not Found")
                        .message(ex.getMessage())
                        .path(request.getPath().toString())
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAlreadyExists(ResourceAlreadyExistsException ex, ServerHttpRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error("Conflict")
                        .message(ex.getMessage())
                        .path(request.getPath().toString())
                        .timestamp(LocalDateTime.now())
                        .build()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationErrors(WebExchangeBindException ex, ServerHttpRequest request) {
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (v1, v2) -> v1));

        return Mono.just(ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Validation Failed")
                        .message("One or more fields are invalid")
                        .path(request.getPath().toString())
                        .timestamp(LocalDateTime.now())
                        .validationErrors(errors)
                        .build()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex, ServerHttpRequest request) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("Internal Server Error")
                        .message(ex.getMessage())
                        .path(request.getPath().toString())
                        .timestamp(LocalDateTime.now())
                        .build()));
    }
}
