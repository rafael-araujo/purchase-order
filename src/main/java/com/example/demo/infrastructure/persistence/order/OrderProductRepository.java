package com.example.demo.infrastructure.persistence.order;

import com.example.demo.domain.entity.order.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {

    @Query("SELECT SUM(op.amount) FROM OrderProductEntity op JOIN op.order o WHERE o.status = 'ABERTO'")
    Long sumAmountByOrderStatus();

    @Query("SELECT op FROM OrderProductEntity op JOIN op.order o WHERE o.status = 'ABERTO'")
    List<OrderProductEntity> findByStatusOpen();
}
