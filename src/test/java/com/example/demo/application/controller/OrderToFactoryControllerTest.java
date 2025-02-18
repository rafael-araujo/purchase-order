package com.example.demo.application.controller;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.error.ErrorResponse;
import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.request.OrderToFactoryRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import com.example.demo.domain.service.OrderToFactoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderToFactoryControllerTest {

    @Mock
    private OrderToFactoryService orderToFactoryService;

    @InjectMocks
    private OrderToFactoryController orderToFactoryController;

    private OrderToFactoryRequest orderToFactoryRequest;

    @BeforeEach
    public void setUp() {
        orderToFactoryRequest = new OrderToFactoryRequest();
    }

    @Test
    public void testCreateOrderToFactory_Success() {
        OrderToFactoryClientResponse orderToFactoryClientResponse = new OrderToFactoryClientResponse();
        orderToFactoryClientResponse.setProtocol("PROTOCOL123");

        when(orderToFactoryService.createOrderToFactory(any(OrderToFactoryRequest.class))).thenReturn(orderToFactoryClientResponse);

        ResponseEntity<?> responseEntity = orderToFactoryController.createOrderToFactory(orderToFactoryRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().getLocation().toString().contains("/api/orders-factory/PROTOCOL123"));
    }

    @Test
    public void testCreateOrderToFactory_ValidationFailure() {

        when(orderToFactoryService.createOrderToFactory(any())).thenThrow(BusinessException.builder()
                .errorResponse(
                        ErrorResponse.builder()
                                .status(404)
                                .build()
                )
                .build());

        OrderToFactoryRequest invalidRequest = new OrderToFactoryRequest();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> orderToFactoryController.createOrderToFactory(invalidRequest),
                "Deve lançar BusinessException quando o revendedor não for encontrado"
        );
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }
}
