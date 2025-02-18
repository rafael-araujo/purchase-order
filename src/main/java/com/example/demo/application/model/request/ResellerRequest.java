package com.example.demo.application.model.request;

import com.example.demo.application.validate.OnPost;
import com.example.demo.domain.model.reseller.AddressDTO;
import com.example.demo.domain.model.reseller.ContactDTO;
import com.example.demo.domain.model.reseller.PhoneNumberDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResellerRequest {

    @NotNull(message = "O campo CNPJ não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo CNPJ não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo CNPJ não pode estar vazio", groups = {OnPost.class})
    private String cnpj;

    @NotNull(message = "O campo RAZÃO SOCIAL não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo RAZÃO SOCIAL não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo RAZÃO SOCIAL não pode estar vazio", groups = {OnPost.class})
    private String corporateName;

    @NotNull(message = "O campo NOME FANTASIA não pode estar nulo", groups = {OnPost.class})
    @NotBlank(message = "O campo NOME FANTASIA não pode estar em branco", groups = {OnPost.class})
    @NotEmpty(message = "O campo NOME FANTASIA não pode estar vazio", groups = {OnPost.class})
    private String fantasyName;

    @Email(message = "O campo EMAIL não pode estar nulo", groups = {OnPost.class})
    @NotNull(message = "O campo EMAIL não pode estar nulo", groups = {OnPost.class})
    @NotEmpty(message = "O campo EMAIL não pode estar vazio", groups = {OnPost.class})
    private String email;

    @Valid
    private List<PhoneNumberDTO> phones ;

    @Valid
    private List<ContactDTO> contacts;

    @Valid
    private List<AddressDTO> address;
}
