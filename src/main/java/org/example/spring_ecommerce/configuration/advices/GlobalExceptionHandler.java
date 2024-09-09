package org.example.spring_ecommerce.configuration.advices;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

        // Trata exceções do tipo IllegalArgumentException, retornando um erro 400 (Bad Request)
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Trata exceções do tipo NullPointerException, retornando um erro 500 (Internal Server Error)
        @ExceptionHandler(NullPointerException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Trata exceções de validação de argumentos de método, retornando um erro 400 (Bad Request)
        // Coleta todos os erros de validação e os inclui na resposta
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
            StringBuilder errors = new StringBuilder();
            for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            ErrorResponse errorResponse = new ErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Trata exceções de entidade não encontrada, retornando um erro 404 (Not Found)
        // Usado quando uma entidade do banco de dados não é localizada
        @ExceptionHandler(EntityNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Trata exceções de acesso negado, retornando um erro 403 (Forbidden)
        // Usado quando um usuário tenta acessar um recurso sem a permissão adequada
        @ExceptionHandler(AccessDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Access denied", HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        // Trata exceções de método HTTP não suportado, retornando um erro 405 (Method Not Allowed)
        // Usado quando o método HTTP (GET, POST, etc.) não é permitido no endpoint
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
        public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
        }

        // Trata exceções de violações de restrição de validação (por exemplo, @NotNull), retornando erro 400 (Bad Request)
        // Usado para capturar falhas em validações de campos
        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Trata exceções de violação de integridade de dados no banco de dados (por exemplo, tentativa de inserir um registro duplicado), retornando erro 409 (Conflict)
        @ExceptionHandler(DataIntegrityViolationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
            ErrorResponse errorResponse = new ErrorResponse("Data integrity violation", HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

    }
