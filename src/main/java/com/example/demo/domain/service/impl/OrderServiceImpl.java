package com.example.demo.domain.service.impl;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.response.OrderResponse;
import com.example.demo.domain.entity.order.ClientEntity;
import com.example.demo.domain.entity.order.OrderProductEntity;
import com.example.demo.domain.entity.order.ProductEntity;
import com.example.demo.domain.entity.order.PurchaseOrderEntity;
import com.example.demo.domain.enums.StatusOrderEnum;
import com.example.demo.domain.model.order.ProductDTO;
import com.example.demo.domain.service.OrderService;
import com.example.demo.domain.utils.GenerateProtocol;
import com.example.demo.infrastructure.persistence.order.ClientRepository;
import com.example.demo.infrastructure.persistence.order.OrderProductRepository;
import com.example.demo.infrastructure.persistence.order.PruchaseOrderRepository;
import com.example.demo.infrastructure.persistence.order.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PruchaseOrderRepository pruchaseOrderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        logger.info("Iniciando criação do pedido para o cliente com ID: {}", request.getClientId());

        Optional<ClientEntity> clientEntity = clientRepository.findById(request.getClientId());
        PurchaseOrderEntity createdOrder;

        if (clientEntity.isPresent()) {
            logger.info("Cliente encontrado com ID: {}", request.getClientId());

            List<ProductDTO> list = request.getProducts();

            if (list.isEmpty()) {
                logger.warn("A lista de produtos está vazia para o pedido do cliente com ID: {}", request.getClientId());
                throw new BusinessException(
                        "Pedido não cadastrado",
                        "A lista de produtos está vazia",
                        HttpStatus.NOT_FOUND.value()
                );
            } else {
                logger.info("Criando novo pedido para o cliente com ID: {}", request.getClientId());

                PurchaseOrderEntity newOrder = new PurchaseOrderEntity();

                newOrder.setClient(clientEntity.get());
                newOrder.setStatus(StatusOrderEnum.ABERTO);

                createdOrder = pruchaseOrderRepository.save(newOrder);
                createdOrder.setDate(LocalDateTime.now());
                createdOrder.setProtocol(GenerateProtocol.generateProtocol(createdOrder));

                pruchaseOrderRepository.save(createdOrder);

                list.forEach(product -> {
                    Optional<ProductEntity> productEntity = productRepository.findById(product.getProductId());
                    if (productEntity.isEmpty()) {
                        logger.warn("Produto não encontrado com ID: {}", product.getProductId());
                        throw new BusinessException(
                                "Pedido não cadastrado",
                                "Não foi localizado produto com id = " + product.getProductId(),
                                HttpStatus.NOT_FOUND.value()
                        );
                    } else if (Objects.isNull(product.getAmount()) || product.getAmount() <= 0) {
                        logger.warn("Quantidade inválida para o produto com ID: {}", product.getProductId());
                        throw new BusinessException(
                                "Pedido não cadastrado",
                                "O produto com id = " + product.getProductId() + " está com a quantidade inválida",
                                HttpStatus.NOT_FOUND.value()
                        );
                    } else {
                        OrderProductEntity orderProductEntity = new OrderProductEntity();
                        orderProductEntity.setOrder(createdOrder);
                        orderProductEntity.setProduct(productEntity.get());
                        orderProductEntity.setAmount(product.getAmount());

                        orderProductRepository.save(orderProductEntity);
                        logger.info("Produto adicionado ao pedido: {} - Quantidade: {}", product.getProductId(), product.getAmount());
                    }
                });
            }
        } else {
            logger.warn("Cliente não encontrado com ID: {}", request.getClientId());
            throw new BusinessException(
                    "Pedido não cadastrado",
                    "Não foi localizado cliente com id = " + request.getClientId(),
                    HttpStatus.NOT_FOUND.value()
            );
        }

        logger.info("Pedido criado com sucesso para o cliente com ID: {} - Protocolo: {}", request.getClientId(), createdOrder.getProtocol());
        return new OrderResponse(createdOrder.getProtocol());
    }
}
