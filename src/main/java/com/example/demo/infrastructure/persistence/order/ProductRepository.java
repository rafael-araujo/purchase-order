package com.example.demo.infrastructure.persistence.order;

import com.example.demo.domain.entity.order.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
