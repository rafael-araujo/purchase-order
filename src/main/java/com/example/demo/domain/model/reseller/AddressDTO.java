package com.example.demo.domain.model.reseller;

import com.example.demo.application.validate.OnPost;
import com.example.demo.application.validate.OnPut;
import com.example.demo.domain.entity.reseller.AddressEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    @NotNull(message = "O id do ENDEREÇO não pode ser nulo", groups = {OnPut.class})
    private Long id;

    @NotNull(message = "O campo RUA não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo RUA não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo RUA não pode estar vazio", groups = {OnPost.class})
    private String street;

    @NotNull(message = "O campo NÚMERO não pode estar nulo", groups = {OnPost.class})
    private Integer number;

    @NotNull(message = "O campo CIDADE não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo CIDADE não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo CIDADE não pode estar vazio", groups = {OnPost.class})
    private String city;

    @NotNull(message = "O campo UF não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo UF não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo UF não pode estar vazio", groups = {OnPost.class})
    private String state;

    @NotNull(message = "O campo CEP não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo CEP não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo CEP não pode estar vazio", groups = {OnPost.class})
    private String postalCode;

    @NotNull(message = "O campo PAÍS não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo PAÍS não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo PAÍS não pode estar vazio", groups = {OnPost.class})
    private String country;

    public AddressDTO(AddressEntity entity) {

        if (Objects.nonNull(entity)) {

            if (Objects.nonNull(entity.getId())) {
                this.id = entity.getId();
            }

            this.street = entity.getStreet();
            this.number = entity.getNumber();
            this.city = entity.getCity();
            this.state = entity.getState();
            this.postalCode = entity.getPostalCode();
            this.country = entity.getCountry();
        }
    }
}
