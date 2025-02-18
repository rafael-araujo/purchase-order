package com.example.demo.application.model.response;

import com.example.demo.domain.entity.reseller.ResellerEntity;
import com.example.demo.domain.model.reseller.AddressDTO;
import com.example.demo.domain.model.reseller.ContactDTO;
import com.example.demo.domain.model.reseller.PhoneNumberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResellerResponse {

    private Long id;
    private Long cnpj;
    private String corporateName;
    private String fantasyName;
    private String email;
    private List<PhoneNumberDTO> phones ;
    private List<ContactDTO> contacts;
    private List<AddressDTO> address;

    public ResellerResponse(ResellerEntity entity) {

        this.id = entity.getId();
        this.cnpj = entity.getCnpj();
        this.corporateName = entity.getCorporateName();
        this.fantasyName = entity.getFantasyName();
        this.email = entity.getEmail();

        this.phones = (entity.getPhones().stream()
                .map(PhoneNumberDTO::new)
                .collect(Collectors.toList()));

        this.contacts = (entity.getContacts().stream()
                .map(ContactDTO::new)
                .collect(Collectors.toList()));

        this.address = (entity.getAddress().stream()
                .map(AddressDTO::new)
                .collect(Collectors.toList()));
    }
}
