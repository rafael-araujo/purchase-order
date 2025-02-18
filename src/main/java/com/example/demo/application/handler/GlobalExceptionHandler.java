package com.example.demo.application.handler;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {

        return new ResponseEntity<>(ex.getErrorResponse(),
                HttpStatusCode.valueOf(HttpStatus.resolve(ex.getErrorResponse().getStatus()) != null
                        ? ex.getErrorResponse().getStatus() : 500));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, ErrorResponse> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().stream()
            .filter(error -> error instanceof FieldError)
            .forEach(error -> this.mapError(error, errors));

        return new ResponseEntity<>(this.returnError(errors), HttpStatus.BAD_REQUEST);
    }

    public void mapError(ObjectError error, Map<String, ErrorResponse> errors) {

        errors.put(error.getCode(), ErrorResponse.builder()
                .title("Erro na validação de campos")
                .description(error.getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now().toString())
                .build());
    }

    public ErrorResponse returnError(Map<String, ErrorResponse> errors) {

        ErrorResponse error = new ErrorResponse();

        if (errors.containsKey("NotNull") && !errors.containsKey("NotEmpty") && !errors.containsKey("NotBlank")){
            error = errors.get("NotNull");
            return error;
        }

        if (errors.containsKey("NotNull") && errors.containsKey("NotEmpty") && errors.containsKey("NotBlank")){
            error = errors.get("NotNull");
            return error;
        }

        if (!errors.containsKey("NotNull") && errors.containsKey("NotEmpty") && errors.containsKey("NotBlank")){
            error = errors.get("NotEmpty");
            return error;
        }

        if (!errors.containsKey("NotNull") && !errors.containsKey("NotEmpty") && errors.containsKey("NotBlank")){
            error = errors.get("NotBlank");
            return error;
        }

        return error;
    }
}
