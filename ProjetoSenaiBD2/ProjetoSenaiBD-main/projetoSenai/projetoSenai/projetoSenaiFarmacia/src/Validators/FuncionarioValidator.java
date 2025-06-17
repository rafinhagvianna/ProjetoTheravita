package Validators;

public class FuncionarioValidator {
	
    public static boolean isValidNome(String nome) {
    	
        if (nome.length() < 5) {
            System.out.println("O nome deve ter ao menos 5 letras");
            return false;
        }
        // Verifica se o nome informado é vazio.
        if(nome == null || nome.trim().isEmpty()) {
        	System.out.println("O nome não pode estar vazio");
        }
        
        // Verifica que o nome informado é composto apenas por letras.
        if (!nome.matches("[a-zA-Z\\s]+")) {
            System.out.println("O nome deve conter apenas letras. Números não são permitidos.");
            return false;
        }

        return true;
    }
    public static boolean isValidCpf(String cpf) {

        // 1. Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            System.out.println("O CPF deve ter ao menos 11 digitos");
            return false;
        }

        if (!cpf.matches("(\\d){11}")) {
            System.out.println("O CPF deve ter ao menos 11 digitos numéricos");
            return false;
        }
        
        return true;
    }
    public static boolean isValidGenero(String genero) {        
        if (genero.equalsIgnoreCase("H")  || 
            genero.equalsIgnoreCase("M") || 
            genero.equalsIgnoreCase("O")) {
            return true;
        }
        System.out.println("O gênero deve ser (H) homem, (M) mulher ou (O) outro! ");
        return false;
    }

    public static boolean isValidSalario(double salario) {        
        if (salario < 1500){
            System.out.println("O salário deve ser ao menos 1500!");
            return false;
        }
        return true;
    }
}
