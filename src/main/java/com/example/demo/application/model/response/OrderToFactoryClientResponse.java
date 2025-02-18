package com.example.demo.application.model.response;//package com.example.demo.application.model.response;

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
public class OrderToFactoryClientResponse {

    private String protocol;
    private List<ProductDTO> products;
}
