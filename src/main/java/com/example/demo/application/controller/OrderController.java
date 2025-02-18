package com.example.demo.application.controller;

import com.example.demo.application.model.request.OrderRequest;
import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.OrderResponse;
import com.example.demo.application.model.response.ResellerResponse;
import com.example.demo.application.validate.OnPost;
import com.example.demo.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@Validated(OnPost.class) @RequestBody OrderRequest request) {
        OrderResponse newOrder = orderService.createOrder(request);
        return ResponseEntity.created(URI.create(
                String.format("/api/orders/%s", newOrder.getProtocol()))).build();
    }
}
