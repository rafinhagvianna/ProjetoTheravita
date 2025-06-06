

import java.util.Scanner;

import Validators.FuncionarioValidator;

public class FuncionarioException extends Exception{

    Scanner scanner = new Scanner(System.in);

    public Funcionario CadastroException(String nome, String cpf, String genero, Setor setorSelecionado){

        if (!FuncionarioValidator.isValidNome(nome)) {
            System.out.println("Insira o nome: ");
            nome = scanner.next();
        }
        if (!FuncionarioValidator.isValidCpf(cpf)) {
            System.out.println("Insira o CPF: ");
            cpf = scanner.next();
        }
        if (!FuncionarioValidator.isValidGenero(genero)) {
            System.out.println("Insira o gênero: ");
            genero = scanner.next();
        }

        try {
            return new Funcionario(nome, cpf, genero, setorSelecionado, null);
        } catch (FuncionarioException funEx) {
            funEx.CadastroException(nome, cpf, genero, setorSelecionado);
        }
        return null;
    }


    public String exceptionNome(){
        System.out.println("Nome inválido, ");
        return"";
    }

    public void CpfException(Funcionario func) {
        try{
            func.setCpf(scanner.next());
        }catch(FuncionarioException nte){
            nte.CpfException(func);
		}
    }
}
    