package com.example.demo.domain.entity.reseller;

import com.example.demo.domain.model.reseller.AddressDTO;
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
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resellerId", nullable = false)
    private ResellerEntity reseller;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postalCode", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    public AddressEntity(AddressDTO dto, ResellerEntity entity) {

        this.id = dto.getId();
        this.reseller = entity;
        this.street = dto.getStreet();
        this.number = dto.getNumber();
        this.city = dto.getCity();
        this.state = dto.getState();
        this.postalCode = dto.getPostalCode();
        this.country = dto.getCountry();
    }

}
