package com.example.demo.domain.model.reseller;

import com.example.demo.application.validate.OnPut;
import com.example.demo.domain.entity.reseller.PhoneNumberEntity;
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
public class PhoneNumberDTO {

    @NotNull(message = "O id do TELEFONE não pode ser nulo", groups = {OnPut.class})
    private Long id;

    private Long phoneNumber;

    public PhoneNumberDTO(PhoneNumberEntity entity) {

        if (Objects.nonNull(entity)) {
            if (Objects.nonNull(entity.getId())) {
                this.id = entity.getId();
            }

            if (Objects.nonNull(entity.getPhoneNumber())) {
                this.phoneNumber = entity.getPhoneNumber();
            }
        }
    }
}
