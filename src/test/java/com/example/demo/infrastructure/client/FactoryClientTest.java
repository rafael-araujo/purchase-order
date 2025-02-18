package com.example.demo.infrastructure.client;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderToFactoryClientRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactoryClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RetryTemplate retryTemplate;

    @InjectMocks
    private FactoryClient factoryClient;

    private OrderToFactoryClientRequest requestClient;

    @BeforeEach
    public void setUp() {
        requestClient = new OrderToFactoryClientRequest();
        // Adicione dados necessários ao requestClient aqui
    }

    @Test
    public void testCreateOrderToFactory_Success() {
        OrderToFactoryClientResponse responseClient = new OrderToFactoryClientResponse();
        ResponseEntity<OrderToFactoryClientResponse> responseEntity = new ResponseEntity<>(responseClient, HttpStatus.OK);

        when(retryTemplate.execute(any())).thenReturn(responseEntity);

        OrderToFactoryClientResponse response = factoryClient.createOrderToFactory(requestClient);

        assertNotNull(response);
        verify(retryTemplate, times(1)).execute(any());
    }

    @Test
    public void testCreateOrderToFactory_Failure() {
        when(retryTemplate.execute(any())).thenThrow(new RestClientException("Failed to create order"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            factoryClient.createOrderToFactory(requestClient);
        });

        assertEquals("Pedido na fábrica não solicitado", exception.getErrorResponse().getTitle());
        assertEquals("Falha ao tentar chamar (3x) o serviço de criação de pedido na fábrica", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception.getErrorResponse().getStatus());
        verify(retryTemplate, times(1)).execute(any());
    }
}

