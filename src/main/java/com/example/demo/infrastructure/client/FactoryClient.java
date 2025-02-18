package com.example.demo.infrastructure.client;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderToFactoryClientRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import com.example.demo.domain.service.impl.OrderServiceImpl;
import com.example.demo.infrastructure.persistence.order.OrderProductRepository;
import com.example.demo.infrastructure.persistence.order.PruchaseOrderRepository;
import com.example.demo.infrastructure.persistence.reseller.ResellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class FactoryClient {

    Logger logger = LoggerFactory.getLogger(FactoryClient.class);

    private static final Long MINIMUM_AMOUNT= 1000L;

    @Value("${factory.url}")
    private String url;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private PruchaseOrderRepository pruchaseOrderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    public OrderToFactoryClientResponse createOrderToFactory(OrderToFactoryClientRequest requestClient) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<OrderToFactoryClientRequest> req = new HttpEntity<>(requestClient, headers);

        try {
            ResponseEntity<OrderToFactoryClientResponse> response = retryTemplate.execute(context ->
                    restTemplate.postForEntity(url, req, OrderToFactoryClientResponse.class)
            );

            return response.getBody();

        } catch (RestClientException e) {
            throw new BusinessException(
                "Pedido na fábrica não solicitado",
                "Falha ao tentar chamar (3x) o serviço de criação de pedido na fábrica",
                HttpStatus.SERVICE_UNAVAILABLE.value());
        }
    }
}

