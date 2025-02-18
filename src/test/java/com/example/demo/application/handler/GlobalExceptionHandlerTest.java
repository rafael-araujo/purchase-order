package com.example.demo.application.handler;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.error.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private BusinessException businessException;
    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse("Erro", "Detalhe do erro", 409);
        businessException = new BusinessException("Erro de Negócio", "Detalhe do erro de negócio", 409);
        businessException.setErrorResponse(errorResponse);
    }

    @Test
    void testHandleBusinessException() {
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(businessException);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("objectName", "field1", "message1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "message2");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        Map<String, ErrorResponse> expectedErrors = new HashMap<>();
        globalExceptionHandler.mapError(fieldError1, expectedErrors);
        globalExceptionHandler.mapError(fieldError2, expectedErrors);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private void mapError(ObjectError error, Map<String, ErrorResponse> errors) {
        FieldError fieldError = (FieldError) error;
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        errors.put(field, new ErrorResponse("Erro de Validação", defaultMessage, HttpStatus.BAD_REQUEST.value()));
    }
}
