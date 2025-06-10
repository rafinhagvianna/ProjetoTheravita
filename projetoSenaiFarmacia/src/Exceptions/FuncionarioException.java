package Exceptions;

import java.util.Scanner;

import Classes.Funcionario;
import Classes.Setor;
import Validators.FuncionarioValidator;

public class FuncionarioException extends Exception{

    Scanner scanner = new Scanner(System.in);

    public Funcionario CadastroException(String nome, String cpf, String genero, Setor setorSelecionado){

        // Validação do nome
        while (!FuncionarioValidator.isValidNome(nome)) {
            System.out.println("Insira o nome completo:");
            nome = scanner.next();
        }
        // Validação do CPF
        while (!FuncionarioValidator.isValidCpf(cpf)) {
            System.out.println("O CPF deve ter ao menos 11 digitos");
            System.out.println("Insira o CPF:");
            cpf = scanner.next();
        }
        // Validação do gênero
        while (!FuncionarioValidator.isValidGenero(genero)) {
            System.out.println("O gênero deve ser (H) homem, (M) mulher ou (O) outro!");
            System.out.println("Insira o gênero:");
            genero = scanner.next();
        }

        // Criação do funcionário
        try {
            return new Funcionario(nome, cpf, genero, setorSelecionado, null);
        } catch (FuncionarioException funEx) {
            // Não recursivo, apenas retorna null em caso de erro
            return null;
        }
    }

    public String exceptionNome(){
        System.out.println("Nome inválido.");
        return "";
    }

    public void CpfException(Funcionario func) {
        try{
            func.setCpf(scanner.next());
        }catch(FuncionarioException nte){
            // Evite recursão infinita
            System.out.println("CPF inválido.");
        }
    }
}
