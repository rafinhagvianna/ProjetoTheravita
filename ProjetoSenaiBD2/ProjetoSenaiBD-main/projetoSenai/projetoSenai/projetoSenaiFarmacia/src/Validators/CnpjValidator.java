package Validators;

// Remova as importações não utilizadas, pois a classe Transportadora e ArrayList não são mais necessárias aqui
// import Classes.Transportadora;
// import java.util.ArrayList;

public class CnpjValidator {

  
    public static boolean isValidFormat(String cnpj) {
        // 1. Remove caracteres não numéricos do CNPJ
        String cnpjNumeros = cnpj.replaceAll("[^0-9]", "");

        // 2. Verifica se o CNPJ tem 14 dígitos
        if (cnpjNumeros.length() != 14) {
            return false;
        }

        // 3. Verifica se todos os dígitos são iguais (ex: "00000000000000" é inválido)
        // Isso evita CNPJs como 11.111.111/1111-11
        if (cnpjNumeros.matches("(\\d)\\1{13}")) {
            return false;
        }
        return true; 
    }
}