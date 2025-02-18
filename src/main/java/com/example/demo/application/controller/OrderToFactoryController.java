package com.example.demo.application.controller;

import com.example.demo.application.model.request.OrderToFactoryRequest;
import com.example.demo.application.model.response.OrderToFactoryClientResponse;
import com.example.demo.application.validate.OnPost;
import com.example.demo.domain.service.OrderToFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/orders-factory")
public class OrderToFactoryController {

    @Autowired
    private OrderToFactoryService orderToFactoryService;

    @PostMapping
    public ResponseEntity<?> createOrderToFactory(@Validated(OnPost.class) @RequestBody OrderToFactoryRequest request) {
        OrderToFactoryClientResponse response = orderToFactoryService.createOrderToFactory(request);
        return ResponseEntity.created(URI.create(
                String.format("/api/orders-factory/%s", response.getProtocol()))).build();
    }
}
