package com.example.demo.infrastructure.persistence.order;

import com.example.demo.domain.entity.order.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PruchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

}
