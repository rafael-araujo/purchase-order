package com.example.demo.domain.service;

import com.example.demo.application.model.request.OrderToFactoryRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;

public interface OrderToFactoryService {

    OrderToFactoryClientResponse createOrderToFactory(OrderToFactoryRequest request);
}
