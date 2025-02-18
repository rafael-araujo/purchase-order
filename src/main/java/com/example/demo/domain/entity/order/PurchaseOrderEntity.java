package com.example.demo.domain.entity.order;

import com.example.demo.domain.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchase_order")
public class PurchaseOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String protocol;

    @Enumerated(EnumType.STRING)
    private StatusOrderEnum status;

    @ManyToOne
    private ClientEntity client;

    private LocalDateTime date;

}
