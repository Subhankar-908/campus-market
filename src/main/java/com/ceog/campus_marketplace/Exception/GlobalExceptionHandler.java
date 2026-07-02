package com.ceog.campus_marketplace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Validation errors (@Valid fails) ──────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field   = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.builder()
                        .status(400)
                        .error("Validation Failed")
                        .message("One or more fields are invalid")
                        .fieldErrors(fieldErrors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ── Business logic errors (user not found, already exists, etc.) ──────
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> fieldErrors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String field   = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            fieldErrors.put(field, message);
//        });
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(ApiErrorResponse.builder()
//                        .status(400)
//                        .error("Validation Failed")
//                        .message("One or more fields are invalid")
//                        .fieldErrors(fieldErrors)
//                        .timestamp(LocalDateTime.now())
//                        .build());
//    }
    // ── Wrong username / password ─────────────────────────────────────────
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorResponse.builder()
                        .status(401)
                        .error("Unauthorized")
                        .message("Invalid username or password")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ── @PreAuthorize fails (wrong role) ──────────────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.builder()
                        .status(403)
                        .error("Forbidden")
                        .message("You do not have permission to perform this action")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ── Already-admin check ───────────────────────────────────────────────
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalAccess(IllegalAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.builder()
                        .status(409)
                        .error("Conflict")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ── Catch-all (unexpected errors) ─────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.builder()

                        .status(500)
                        .error("Internal Server Error")
                        .message("Something went wrong. Please try again later.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}