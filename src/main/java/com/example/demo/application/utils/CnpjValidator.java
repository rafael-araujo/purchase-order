package com.example.demo.application.utils;

public class CnpjValidator {

    public static boolean isValidCnpj(String cnpjOriginal) {

        if (cnpjOriginal == null) {
            return false;
        }

        String cnpj = cnpjOriginal.replaceAll("[^\\d]", "");

        if (cnpj.length() != 14) {
            return false;
        }

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            return false;
        }

        int[] weight = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int digit1 = digitCalculator(cnpj.substring(0, 12), weight);
        int digit2 = digitCalculator(cnpj.substring(0, 12) + digit1, weight);

        return cnpj.equals(cnpj.substring(0, 12) + Integer.toString(digit1) + Integer.toString(digit2));
    }

    private static int digitCalculator(String str, int[] weight) {
        int count = 0;
        for (int i = str.length() - 1, digito; i >= 0; i--) {
            digito = Integer.parseInt(str.substring(i, i + 1));
            count += digito * weight[weight.length - str.length() + i];
        }
        count = 11 - count % 11;
        return (count > 9) ? 0 : count;
    }
}