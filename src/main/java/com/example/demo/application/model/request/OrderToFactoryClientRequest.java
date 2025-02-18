package com.example.demo.application.model.request;

import com.example.demo.domain.model.order.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderToFactoryClientRequest {

    private Long resellerId;
    private List<ProductDTO> products;
}
