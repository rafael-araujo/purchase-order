package com.example.demo.application.validate;

import com.example.demo.application.utils.CnpjValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CnpjValidatorTest {

    @Test
    public void testValidCnpj() {

        String formattedCnpj = "04.252.011/0001-10";
        String plainCnpj = "04252011000110";

        assertTrue(CnpjValidator.isValidCnpj(formattedCnpj), "CNPJ formatado válido deve retornar true");
        assertTrue(CnpjValidator.isValidCnpj(plainCnpj), "CNPJ sem formatação válido deve retornar true");
    }

    @Test
    public void testInvalidCnpjChecksum() {
        String invalidCnpj = "04252011000111";
        assertFalse(CnpjValidator.isValidCnpj(invalidCnpj), "CNPJ com dígito verificador inválido deve retornar false");
    }

    @Test
    public void testInvalidCnpjLength() {
        String shortCnpj = "123456789012";
        assertFalse(CnpjValidator.isValidCnpj(shortCnpj), "CNPJ com menos de 14 dígitos deve retornar false");
    }

    @Test
    public void testInvalidCnpjNonNumeric() {
        String invalidCnpj = "AA.BB.CC/DDDD-EE";
        assertFalse(CnpjValidator.isValidCnpj(invalidCnpj), "CNPJ contendo caracteres não numéricos deve retornar false");
    }

    @Test
    public void testNullCnpj() {
        assertFalse(CnpjValidator.isValidCnpj(null), "CNPJ nulo deve retornar false");
    }
}
