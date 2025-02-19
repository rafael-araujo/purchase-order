package com.example.demo.domain.service.impl;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderToFactoryClientRequest;
import com.example.demo.application.model.request.OrderToFactoryRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import com.example.demo.domain.entity.order.OrderProductEntity;
import com.example.demo.domain.entity.reseller.ResellerEntity;
import com.example.demo.domain.enums.StatusOrderEnum;
import com.example.demo.domain.model.order.ProductDTO;
import com.example.demo.domain.service.OrderToFactoryService;
import com.example.demo.infrastructure.client.FactoryClient;
import com.example.demo.infrastructure.persistence.order.OrderProductRepository;
import com.example.demo.infrastructure.persistence.reseller.ResellerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderToFactoryServiceImpl implements OrderToFactoryService {

    Logger logger = LoggerFactory.getLogger(OrderToFactoryServiceImpl.class);

    private static final Long MINIMUM_AMOUNT = 1000L;

    @Value("${factory.url}")
    private String url;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private FactoryClient factoryClient;

    @Transactional
    public OrderToFactoryClientResponse createOrderToFactory(OrderToFactoryRequest request) {
        logger.info("Iniciando a criação de pedido na fábrica para o revendedor com ID: {}", request.getResellerId());

        Optional<ResellerEntity> resellerEntity = resellerRepository.findById(request.getResellerId());

        if (resellerEntity.isEmpty()) {
            logger.warn("Revendedor não encontrado com ID: {}", request.getResellerId());
            throw new BusinessException(
                    "Pedido na fábrica não solicitado",
                    "Não foi localizado o revendedor com id = " + request.getResellerId(),
                    HttpStatus.NOT_FOUND.value()
            );
        }

        List<OrderProductEntity> list = orderProductRepository.findByStatusOpen();
        logger.info("Total de pedidos abertos encontrados: {}", list.size());

        OrderToFactoryClientRequest requestClient = new OrderToFactoryClientRequest();

        Long sum = list.stream()
                .mapToLong(OrderProductEntity::getAmount)
                .sum();

        if (sum < MINIMUM_AMOUNT) {
            logger.warn("Quantidade de produtos abaixo do valor mínimo de {} produtos", MINIMUM_AMOUNT);
            throw new BusinessException(
                    "Pedido na fábrica não solicitado",
                    "A quantidade de produtos dos pedidos está abaixo do valor mínimo de " + MINIMUM_AMOUNT +
                            " produtos, aguardar novos pedidos para solicitar um pedido à fábrica",
                    HttpStatus.UNPROCESSABLE_ENTITY.value()
            );
        } else {
            logger.info("Quantidade total de produtos: {}", sum);

            Map<Long, Integer> mapProducts = new HashMap<>();

            list.forEach(orderProduct -> {
                Long key = orderProduct.getProduct().getId();

                if (mapProducts.containsKey(key)) {
                    Integer amount = mapProducts.get(key);
                    amount += orderProduct.getAmount();
                    mapProducts.put(key, amount);
                } else {
                    mapProducts.put(key, orderProduct.getAmount());
                }
            });

            requestClient.setResellerId(request.getResellerId());
            requestClient.setProducts(new ArrayList<>());

            mapProducts.forEach((key, value) -> {
                requestClient.getProducts().add(
                        ProductDTO.builder()
                                .productId(key)
                                .amount(value)
                                .build()
                );
            });

            logger.info("Solicitação de pedido à fábrica criada com sucesso: {}", requestClient);
        }

        OrderToFactoryClientResponse response = factoryClient.createOrderToFactory(requestClient);
        logger.info("Pedido na fábrica criado com sucesso, protocolo: {}", response.getProtocol());

        list.forEach(orderProduct -> {
            orderProduct.setFactoryProtocol(response.getProtocol());
            orderProduct.getOrder().setStatus(StatusOrderEnum.SOLICITADO);
            orderProductRepository.save(orderProduct);
        });

        return response;
    }
}
