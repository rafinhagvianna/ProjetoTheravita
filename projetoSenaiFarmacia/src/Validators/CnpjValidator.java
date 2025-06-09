package Validators;


import Classes.Transportadora;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.List;

import static Classes.Transportadora.buscarTransportadora;

public class CnpjValidator {
    public static boolean isValid(String cnpj, ArrayList<Transportadora> transportadoras) {
        // 1. Remove caracteres não numéricos do CNPJ
        String cnpjNumeros = cnpj.replaceAll("[^0-9]", "");

        // 2. Verifica se o CNPJ tem 14 dígitos
        if (cnpjNumeros.length() != 14) {
            return false;
        }

        // 3. Verifica se todos os dígitos são iguais (ex: "00000000000000" é inválido)
        if (cnpjNumeros.matches("(\\d)\\1{13}")) {
            return false;
        }

        return true;
    }
}