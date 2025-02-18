package com.example.demo.domain.entity.reseller;

import com.example.demo.application.model.request.ResellerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reseller")
public class ResellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long cnpj;

    @Column(nullable = false)
    private String corporateName;

    @Column(nullable = false)
    private String fantasyName;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "reseller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumberEntity> phones ;

    @OneToMany(mappedBy = "reseller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactEntity> contacts;

    @OneToMany(mappedBy = "reseller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> address;

    public ResellerEntity(ResellerRequest request) {


        this.cnpj = Long.valueOf(request.getCnpj());
        this.corporateName = request.getCorporateName();
        this.fantasyName = request.getFantasyName();
        this.email = request.getEmail();

        this.phones = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.address = new ArrayList<>();

        request.getPhones().forEach( element -> {
            PhoneNumberEntity phoneNummber = new PhoneNumberEntity(element, this);
            this.phones.add(phoneNummber);
            phoneNummber.setReseller(this);
        });

        request.getContacts().forEach( element -> {
            ContactEntity contact = new ContactEntity(element, this);
            this.contacts.add(contact);
            contact.setReseller(this);
        });

        request.getAddress().forEach( element -> {
            AddressEntity address = new AddressEntity(element, this);
            this.address.add(address);
            address.setReseller(this);
        });
    }
}