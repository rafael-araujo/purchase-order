package com.example.demo.domain.entity.reseller;

import com.example.demo.domain.model.reseller.PhoneNumberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone_number")
public class PhoneNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resellerId", nullable = false)
    private ResellerEntity reseller;

    @Column(name = "phoneNumber")
    private Long phoneNumber;

    public PhoneNumberEntity (PhoneNumberDTO dto, ResellerEntity entity) {

        this.id = dto.getId();
        this.reseller = entity;
        this.phoneNumber = dto.getPhoneNumber();
    }
}
