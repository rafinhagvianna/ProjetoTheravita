package Gerenciadores;

import Interfaces.IntSalario;

import java.util.ArrayList;
import java.util.Scanner;

import Classes.*;

public class GerSalario implements IntSalario {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void menu(){
        System.out.println("Escolha uma das opções: ");
        System.out.println("-------------------------------------------");
        System.out.println("| 1 - Apresentar salário bruto            |");
        System.out.println("| 2 - Calcular salário líquido            |");
        System.out.println("| 3 - Consultar valores dos benefícios    |");
        System.out.println("| 4 - Exibir demonstrativo salarial       |");
        System.out.println("| 5 - Valor da bonificação p/ funcionário |");
        System.out.println("| 0 - Sair                                |");
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    public void salarioBruto(ArrayList<Funcionario> funcionarios) {
        System.out.print("Informe o ID do funcionário: ");
        String idFuncionario = scanner.nextLine();
        Funcionario funcionarioBruto = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

        if (funcionarioBruto != null && funcionarioBruto.getSalario() != null) {
            System.out.println("Salário bruto de " + funcionarioBruto.getNome() + ": R$ " + String.format("%.2f", funcionarioBruto.getSalario().getSalario()));
        } else {
            System.out.println("Funcionário ou salário não encontrado.");
        }
    }

    public void calcularSalario(ArrayList<Funcionario> funcionarios){
        System.out.print("Informe o ID do funcionário: ");
        String idFuncionario = scanner.nextLine();
        Funcionario funcionarioLiquido = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

        if (funcionarioLiquido != null && funcionarioLiquido.getSalario() != null) {
            double salarioLiquido = funcionarioLiquido.getSalario().calculaSalario();
            System.out.println("Salário líquido de " + funcionarioLiquido.getNome() + ": R$ " + String.format("%.2f", salarioLiquido));
        } else {
            System.out.println("Funcionário ou salário não encontrado.");
        }
    }

    public void consultarBeneficios(ArrayList<Funcionario> funcionarios){
        System.out.print("Informe o ID do funcionário: ");
        String idFuncionario = scanner.nextLine();
        Funcionario funcionarioBeneficios = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

        if (funcionarioBeneficios != null && funcionarioBeneficios.getSalario() != null) {
            Salario salarioFuncionario = funcionarioBeneficios.getSalario();
            System.out.println("Benefícios de " + funcionarioBeneficios.getNome() + ":");
            System.out.println(" - Plano de saúde: R$ " + String.format("%.2f", salarioFuncionario.getSaude()));
            System.out.println(" - Vale refeição/alimentação: R$ " + String.format("%.2f", salarioFuncionario.getValeRefAliment()));
            System.out.println(" - Plano odontológico: R$ " + String.format("%.2f", salarioFuncionario.getOdonto()));
        } else {
            System.out.println("Funcionário ou salário não encontrado.");
        }
    }

    public void exibirDemonstrativo(ArrayList<Funcionario> funcionarios){
        System.out.print("Informe o ID do funcionário: ");
        String idFuncionario = scanner.nextLine();
        Funcionario funcionarioDemonstrativo = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

        if (funcionarioDemonstrativo != null) {
            funcionarioDemonstrativo.dadosFuncionario();
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    public void valorBonificacao(Caixa caixa, ArrayList<Setor> setores){
        if (caixa != null && setores != null) {
            System.out.println("\nBonificação por funcionário: " + String.format("%.2f", Salario.calcularBonificacao(caixa, setores)));
        } else {
            System.out.println("Não há registro de caixa ou setor");
        }
    }

    
}
