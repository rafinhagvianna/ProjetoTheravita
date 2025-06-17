package Exceptions;

import java.util.Scanner;

import Classes.Funcionario;
import Classes.Salario;
import Classes.Setor;
import Validators.FuncionarioValidator;

public class FuncionarioException extends Exception {

    Scanner scanner = new Scanner(System.in);

    public Funcionario CadastroException(String nome, String cpf, String genero, Setor setorSelecionado) {
        do {
            System.out.println("Insira o nome completo:");
            nome = scanner.next();
        } while (!FuncionarioValidator.isValidNome(nome));

        do {
            System.out.println("Insira o CPF:");
            cpf = scanner.next();
        } while (!FuncionarioValidator.isValidCpf(cpf));

        do {
            System.out.println("Insira o gênero:");
            genero = scanner.next();
        } while (!FuncionarioValidator.isValidGenero(genero));

        try {
 
            Salario salario = null; 
            return new Funcionario(nome, cpf, genero, setorSelecionado, salario);
        } catch (FuncionarioException e) {
            e.printStackTrace();
            return null; 
        }
    }

    public void NomeException(Funcionario func) {
        try {
            System.out.print("Insira o nome completo: ");
            func.setNome(scanner.next());
        } catch (FuncionarioException nte) {
            nte.NomeException(func);
        }
    }

    public void CpfException(Funcionario func) {
        try {
            System.out.print("Insira o CPF: ");
            func.setCpf(scanner.next());
        } catch (FuncionarioException nte) {
            nte.CpfException(func);
        }
    }

    public void GeneroException(Funcionario func) {
        try {
            System.out.print("Insira o gênero: ");
            func.setGenero(scanner.next());
        } catch (FuncionarioException nte) {
            nte.GeneroException(func);
        }
    }

    public void SalarioException(Funcionario func) {
        try {
            System.out.print("Insira o salário base: ");
            func.getSalario().setSalario(scanner.nextDouble());
            scanner.nextLine();
        } catch (FuncionarioException nte) {
            scanner.nextLine();
            nte.SalarioException(func);
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Tipo inserido inválido! Digite um número real!");
            SalarioException(func);
        }
    }

    public void SalarioException(Salario salario) {
        try {
            System.out.print("Insira o salário base: ");
            salario.setSalario(scanner.nextDouble());
            scanner.nextLine();
        } catch (FuncionarioException nte) {
            scanner.nextLine();
            nte.SalarioException(salario);
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Tipo inserido inválido! Digite um número real!");
            SalarioException(salario);
        }
    }
}