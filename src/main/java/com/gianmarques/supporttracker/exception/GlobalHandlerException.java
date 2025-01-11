package com.gianmarques.supporttracker.exception;


import com.gianmarques.supporttracker.exception.exceptions.EmailUniqueException;
import com.gianmarques.supporttracker.exception.exceptions.PasswordConflictException;
import com.gianmarques.supporttracker.exception.exceptions.PasswordInvalidException;
import com.gianmarques.supporttracker.exception.exceptions.TicketAlreadyClosedException;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(EmailUniqueException.class)
    public ResponseEntity<ErrorMessage> handleUniqueEmailException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(request, HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(request, HttpStatus.NOT_FOUND, exception.getMessage()));
    }


    @ExceptionHandler(PasswordConflictException.class)
    public ResponseEntity<ErrorMessage> handlePasswordConflictInvalidException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(request, HttpStatus.CONFLICT, exception.getMessage()));
    }


    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> handlePasswordInvalidException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, exception.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request, BindingResult result) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Some fields are incorrect", result));
    }

    @ExceptionHandler(TicketAlreadyClosedException.class)
    public ResponseEntity<ErrorMessage> handleTicketAlreadyClosedException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(RuntimeException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, exception.getMessage()));
    }
}
