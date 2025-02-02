package com.example.discordbackend.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        // Construire une réponse JSON avec le message d'erreur
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erreur", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // 409 Conflict
    }
}
