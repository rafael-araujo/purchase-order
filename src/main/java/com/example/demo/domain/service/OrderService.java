package com.example.demo.domain.service;

import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
}
