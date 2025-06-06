package Validators;

public class CpfValidator {
    public static boolean isValid(String cpf) {
        String cpfNumeros = ""+(cpf);

        // 2. Verifica se o CPF tem 11 dígitos
        if (cpfNumeros.length() != 11) {
            return false;
        }

        // 3. Verifica se todos os dígitos são iguais (ex: "000000000000" é inválido)
        // if (cpfNumeros.matches("^\\d{11}$") ) {
        //     return false;
        // }

        return true;
    }
}
