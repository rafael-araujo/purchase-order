package com.example.demo.domain.entity.reseller;


import com.example.demo.domain.model.reseller.ContactDTO;
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
@Table(name = "contact")
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resellerId", nullable = false)
    private ResellerEntity reseller;

    @Column(name = "contactName", nullable = false)
    private String contactName;

    @Column(name = "principal", nullable = false)
    private Boolean principal;

    public ContactEntity(ContactDTO dto, ResellerEntity entity) {
        this.id = dto.getId();
        this.reseller = entity;
        this.contactName = dto.getContactName();
        this.principal = dto.getPrincipal();
    }
}

