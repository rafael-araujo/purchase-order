package com.example.demo.domain.service;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.response.OrderResponse;
import com.example.demo.domain.entity.order.ClientEntity;
import com.example.demo.domain.entity.order.OrderProductEntity;
import com.example.demo.domain.entity.order.ProductEntity;
import com.example.demo.domain.entity.order.PurchaseOrderEntity;
import com.example.demo.domain.model.order.ProductDTO;
import com.example.demo.domain.service.impl.OrderServiceImpl;
import com.example.demo.infrastructure.persistence.order.ClientRepository;
import com.example.demo.infrastructure.persistence.order.OrderProductRepository;
import com.example.demo.infrastructure.persistence.order.PruchaseOrderRepository;
import com.example.demo.infrastructure.persistence.order.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PruchaseOrderRepository pruchaseOrderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setClientId(1L);
    }

    @Test
    public void testCreateOrder_ClientNotFound() {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        assertEquals("Não foi localizado cliente com id = " + orderRequest.getClientId(), exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    public void testCreateOrder_EmptyProductList() {
        ClientEntity clientEntity = new ClientEntity();
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientEntity));
        orderRequest.setProducts(List.of());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        assertEquals("A lista de produtos está vazia", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    public void testCreateOrder_Success() {
        ClientEntity clientEntity = new ClientEntity();
        ProductEntity productEntity = new ProductEntity();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setAmount(10);
        orderRequest.setProducts(List.of(productDTO));

        when(clientRepository.findById(any())).thenReturn(Optional.of(clientEntity));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productEntity));
        when(pruchaseOrderRepository.save(any(PurchaseOrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(orderRequest);

        assertNotNull(response.getProtocol());
        verify(pruchaseOrderRepository, times(2)).save(any(PurchaseOrderEntity.class));
        verify(orderProductRepository, times(1)).save(any(OrderProductEntity.class));
    }

    @Test
    public void testCreateOrder_ProductNotFound() {
        ClientEntity clientEntity = new ClientEntity();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        orderRequest.setProducts(List.of(productDTO));

        when(clientRepository.findById(any())).thenReturn(Optional.of(clientEntity));
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        when(pruchaseOrderRepository.save(any())).thenReturn(PurchaseOrderEntity.builder()
                .id(1L)
                .client(ClientEntity.builder().id(1L).build())
                .build());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        assertEquals("Não foi localizado produto com id = " + productDTO.getProductId(), exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    public void testCreateOrder_InvalidProductAmount() {
        ClientEntity clientEntity = new ClientEntity();
        ProductEntity productEntity = new ProductEntity();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setAmount(0);
        orderRequest.setProducts(List.of(productDTO));
        orderRequest.setClientId(1L);

        when(clientRepository.findById(any())).thenReturn(Optional.of(clientEntity));
        when(productRepository.findById(any())).thenReturn(Optional.of(productEntity));
        when(pruchaseOrderRepository.save(any())).thenReturn(PurchaseOrderEntity.builder()
                .id(1L)
                .client(ClientEntity.builder().id(1L).build())
                .build());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderRequest);
        });

        assertEquals("O produto com id = " + productDTO.getProductId() + " está com a quantidade inválida", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorResponse().getStatus());
    }
}

