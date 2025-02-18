package com.example.demo.domain.model.reseller;

import com.example.demo.application.validate.OnPost;
import com.example.demo.application.validate.OnPut;
import com.example.demo.domain.entity.reseller.ContactEntity;
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
public class ContactDTO {

    @NotNull(message = "O id do CONTATO não pode ser nulo", groups = {OnPut.class})
    private Long id;

    @NotBlank(message = "O campo NOME DO CONTATO não pode estar em branco", groups = {OnPost.class})
    @NotNull(message = "O campo NOME DO CONTATO não pode estar nulo", groups = {OnPost.class})
    @NotEmpty(message = "O campo NOME DO CONTATO não pode estar vazio", groups = {OnPost.class})
    private String contactName;

    @NotNull(message = "O campo CONTATO PRINCIPAL não pode estar nulo", groups = {OnPost.class})
    private Boolean principal;

    public ContactDTO(ContactEntity entity) {

        if (Objects.nonNull(entity)) {

            if (Objects.nonNull(entity.getId())) {
                this.id = entity.getId();
            }

            this.contactName = entity.getContactName();
            this.principal = entity.getPrincipal();
        }
    }
}
