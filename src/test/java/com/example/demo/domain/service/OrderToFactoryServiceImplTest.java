package com.example.demo.domain.service;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderToFactoryClientRequest;
import com.example.demo.application.model.request.OrderToFactoryRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import com.example.demo.domain.entity.order.OrderProductEntity;
import com.example.demo.domain.entity.order.ProductEntity;
import com.example.demo.domain.entity.reseller.ResellerEntity;
import com.example.demo.domain.service.impl.OrderToFactoryServiceImpl;
import com.example.demo.infrastructure.client.FactoryClient;
import com.example.demo.infrastructure.persistence.order.OrderProductRepository;
import com.example.demo.infrastructure.persistence.reseller.ResellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderToFactoryServiceImplTest {

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ResellerRepository resellerRepository;

    @Mock
    private FactoryClient factoryClient;

    @InjectMocks
    private OrderToFactoryServiceImpl orderToFactoryService;

    private OrderToFactoryRequest request;

    @BeforeEach
    public void setUp() {
        request = new OrderToFactoryRequest();
        request.setResellerId(1L);
    }

    @Test
    public void testCreateOrderToFactory_ResellerNotFound() {
        when(resellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderToFactoryService.createOrderToFactory(request);
        });

        assertEquals("Pedido na fábrica não solicitado", exception.getErrorResponse().getTitle());
        assertEquals("Não foi localizado o revendedor com id = " + request.getResellerId(), exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    public void testCreateOrderToFactory_SumBelowMinimumAmount() {
        ResellerEntity resellerEntity = new ResellerEntity();
        when(resellerRepository.findById(anyLong())).thenReturn(Optional.of(resellerEntity));
        when(orderProductRepository.findByStatusOpen()).thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderToFactoryService.createOrderToFactory(request);
        });

        assertEquals("Pedido na fábrica não solicitado", exception.getErrorResponse().getTitle());
        assertEquals("A quantidade de produtos dos pedidos está abaixo do valor mínimo de " + 1000
                + " produtos, aguardar novos pedidos para solicitar um pedido à fábrica", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    public void testCreateOrderToFactory_Success() {
        ResellerEntity resellerEntity = new ResellerEntity();
        when(resellerRepository.findById(anyLong())).thenReturn(Optional.of(resellerEntity));

        OrderProductEntity orderProductEntity = OrderProductEntity.builder()
            .product(ProductEntity.builder()
                .id(1L)
                .build())
            .amount(1000)
            .build();

        List<OrderProductEntity> orderProductList = List.of(orderProductEntity);
        when(orderProductRepository.findByStatusOpen()).thenReturn(orderProductList);

        OrderToFactoryClientResponse responseClient = new OrderToFactoryClientResponse();
        responseClient.setProtocol("PROTOCOL123");
        when(factoryClient.createOrderToFactory(any(OrderToFactoryClientRequest.class))).thenReturn(responseClient);

        OrderToFactoryClientResponse response = orderToFactoryService.createOrderToFactory(request);

        assertNotNull(response);
        assertEquals("PROTOCOL123", response.getProtocol());
        verify(orderProductRepository, times(1)).save(any(OrderProductEntity.class));
    }
}
