package com.bgitu.auth.config;


import com.bgitu.auth.dto.MessageDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDTO> handleValidationExceptions(MethodArgumentNotValidException ex){

        return ResponseEntity.badRequest().body(new MessageDTO(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageDTO> handleUserNotFoundExceptions(UsernameNotFoundException ex){
        return ResponseEntity.badRequest().body(new MessageDTO("User not found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageDTO> handleAccessDeniedExceptions(AccessDeniedException ex){
        return ResponseEntity.status(401).body(new MessageDTO(ex.getMessage()));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageDTO> handleEntityNotFoundExceptions(EntityNotFoundException ex){
        return ResponseEntity.status(400).body(new MessageDTO(ex.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDTO> handleIllegalArgumentExceptionExceptions(IllegalArgumentException ex){
        return ResponseEntity.status(400).body(new MessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<MessageDTO> handleEntityExistsExceptionExceptions(EntityExistsException ex){
        return ResponseEntity.badRequest().body(new MessageDTO(ex.getMessage()));

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageDTO> handleBadCredentialsExceptionExceptions(BadCredentialsException ex){
        return ResponseEntity.status(400).body(new MessageDTO(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(500).body(new MessageDTO("Internal server error: " + ex.getMessage()));
    }

}
