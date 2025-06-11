package Validators;

public class ProdutoValidator {
    public static boolean isValidDescricao(String descricao) {
        if (descricao.length() < 2 || descricao.length() > 30) {
            System.out.println("A descrição deve ter entre 2 letras e 30 letras!");
            return false;
        }
        if (!descricao.matches("^[a-zA-Z0-9 ]+$")) {
            System.out.println("A descrição não deve conter caracteres especiais!");
            return false;
        }

        return true;
    }
    public static boolean isValidValor(double valor) {

        if (valor <= 0) {
            System.out.println("O valor deve ser maior que 0!");
            return false;
        }
        
        return true;
    }
    public static boolean isValidEstoque(int estoque) {        
         if (estoque < 0) {
            System.out.println("O estque deve ter no mínimo 0!");
            return false;
        }
        
        return true;
    }
    public static boolean isValidMinimo(int estoque) {        
         if (estoque < 0) {
            System.out.println("O mínimo do estque deve ser ao menos 0!");
            return false;
        }
        
        return true;
    }
}
