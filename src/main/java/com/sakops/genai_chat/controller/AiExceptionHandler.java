package com.sakops.genai_chat.controller;

import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AiExceptionHandler {

    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<Map<String, String>> handleAiError(NonTransientAiException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        if (ex.getMessage().contains("insufficient_quota")) {
            errorResponse.put("error", "insufficient_quota");
            errorResponse.put("message", "⚠️ You have exceeded your OpenAI quota. Please check your plan or billing.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }

        if (ex.getMessage().contains("rate_limit_exceeded")) {
            errorResponse.put("error", "rate_limit");
            errorResponse.put("message", "⚠️ Too many requests in a short time. Please slow down and try again.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }

        // fallback for other AI errors
        errorResponse.put("error", "ai_error");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
