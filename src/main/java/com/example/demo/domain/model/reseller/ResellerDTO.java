package com.example.demo.domain.model.reseller;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResellerDTO {

    private String oficialName;
    private String fantasyName;
    private String email;
    private List<PhoneNumberDTO> phones ;
    private List<ContactDTO> contacts;
    private List<AddressDTO> Andress;
}
