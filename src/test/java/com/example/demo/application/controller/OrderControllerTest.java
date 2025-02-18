package com.example.demo.application.controller;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.error.ErrorResponse;
import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.response.OrderResponse;
import com.example.demo.domain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        orderRequest = new OrderRequest();
    }

    @Test
    public void testCreateOrder_Success() {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProtocol("PROTOCOL123");

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        ResponseEntity<?> responseEntity = orderController.createOrder(orderRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString().contains("/api/orders/PROTOCOL123"));
    }

    @Test
    public void testCreateOrder_ValidationFailure() {

        when(orderService.createOrder(any())).thenThrow(BusinessException.builder()
            .errorResponse(
                ErrorResponse.builder()
                    .status(404)
                    .build()
            )
        .build());

        OrderRequest invalidRequest = new OrderRequest();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> orderController.createOrder(invalidRequest),
                "Deve lançar BusinessException quando o revendedor não for encontrado"
        );
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }
}


