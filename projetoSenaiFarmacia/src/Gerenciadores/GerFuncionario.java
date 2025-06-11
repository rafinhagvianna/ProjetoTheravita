package Gerenciadores;

import java.util.ArrayList;
import java.util.Scanner;

import Classes.*;
import Exceptions.FuncionarioException;
import Interfaces.IntFuncionario;

public class GerFuncionario implements IntFuncionario {

    public void menu() {
        System.out.println("Escolha uma das opções: ");
        System.out.println("-----------------------------------------");
        System.out.println("| 1 - Cadastrar funcionário             |");
        System.out.println("| 2 - Listar funcionários (por setor)   |");
        System.out.println("| 3 - Editar funcionário                |");
        System.out.println("| 4 - Excluir funcionário               |");
        System.out.println("| 5 - Visualizar total de funcionários  |");
        System.out.println("| 0 - Sair                              |");
        System.out.println("-----------------------------------------");
        System.out.println();
    }

    public void cadastrarFuncionario(Scanner sc, ArrayList<Setor> setores, ArrayList<Funcionario> funcionarios) {
        System.out.print("Nome = ");
        String nome = sc.next();
        System.out.print("CPF = ");
        String cpf = sc.next();
        System.out.print("Gênero = ");
        String genero = sc.next();

        Setor setorSelecionado = null;
        do {
            try {
                System.out.println("Escolha o setor para o funcionário:");
                for (int i = 0; i < setores.size(); i++) {
                    System.out.println((i + 1) + " - " + setores.get(i).getNome());
                }
                int setorEscolhido = sc.nextInt();
                setorSelecionado = setores.get(setorEscolhido - 1);

            } catch (Exception e) {
                System.out.println("Opcção inválida!");
                setorSelecionado = null;
                sc.nextLine();
            }
        } while (setorSelecionado == null);

        double salarioBase = -1;
        do {
            try {
                System.out.print("Salário base = ");
                salarioBase = sc.nextDouble();
            } catch (Exception e) {
                System.out.println("Tipo inserido inválido. Digite um valor real!");
                salarioBase = -1;
                sc.nextLine();
            }
        } while (salarioBase < 0);

        Funcionario funcionario;
        try {
            funcionario = new Funcionario(nome, cpf, genero, setorSelecionado, null);
        } catch (FuncionarioException funEx) {
            funcionario = funEx.CadastroException(nome, cpf, genero, setorSelecionado);
        }

        try {
            Salario salario = new Salario(salarioBase, funcionario);
            funcionario.setSalario(salario);
        } catch (FuncionarioException e) {
            e.SalarioException(funcionario);
        }

        setorSelecionado.getFuncionarios().add(funcionario);

        funcionarios.add(funcionario);
        System.out.println("Funcionário cadastrado com sucesso!");
    }

    public void listarFuncionariosPorSetor(Scanner sc, ArrayList<Setor> setores) {
        int setorEscolhido = -1;
        Setor setorSelecionado = null;
        do {
            System.out.println("Escolha o setor para o funcionário:");
            for (int i = 0; i < setores.size(); i++) {
                System.out.println((i + 1) + " - " + setores.get(i).getNome());
            }
            try {
                setorEscolhido = sc.nextInt();
                if (setorEscolhido < 1 || setorEscolhido > setores.size()) {
                    System.out.println("\nOpção inválida! Tente novamente.");
                    continue;
                }
                setorSelecionado = setores.get(setorEscolhido - 1);
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite um número.");
                sc.nextLine();
            }
        } while (setorSelecionado == null);

        System.out.println("Funcionários do setor " + setorSelecionado.getNome() + ":");
        for (Funcionario func : setorSelecionado.getFuncionarios()) {
            System.out.println(func.toString());
        }
    }

    public void editarFuncionario(Scanner sc, Funcionario funcionarioEditar, ArrayList<Setor> setores) {
        int opc;
        if (funcionarioEditar != null) {
            do {
                System.out.println(
                        "Qual dado deseja modificar? \n1 - Nome\n2 - CPF\n3 - Gênero\n4 - Setor\n5 - Salário\n0 - Sair");

                try {
                    opc = sc.nextInt();
                    switch (opc) {
                        case 1:
                            System.out.print("Insira o novo nome = ");
                            try {

                                funcionarioEditar.setNome(sc.nextLine());
                            } catch (FuncionarioException e) {
                                e.NomeException(funcionarioEditar);
                            }
                            System.out.println("Nome atualizado com sucesso!");
                            break;
                        case 2:
                            System.out.print("Insira o novo CPF = ");
                            try {
                                funcionarioEditar.setCpf(sc.nextLine());
                            } catch (FuncionarioException funEx) {
                                funEx.CpfException(funcionarioEditar);
                            }
                            System.out.println("CPF atualizado com sucesso!");
                            break;
                        case 3:
                            System.out.print("Insira o novo gênero = ");
                            try {
                                funcionarioEditar.setGenero(sc.nextLine());
                            } catch (FuncionarioException e) {
                                e.GeneroException(funcionarioEditar);
                            }
                            System.out.println("Gênero atualizado com sucesso!");
                            break;
                        case 4:
                            Setor setorSelecionado = null;
                            do {
                                try {
                                    System.out.println("Escolha o setor para o funcionário:");
                                    for (int i = 0; i < setores.size(); i++) {
                                        System.out.println((i + 1) + " - " + setores.get(i).getNome());
                                    }
                                    int setorEscolhido = sc.nextInt();
                                    setorSelecionado = setores.get(setorEscolhido - 1);

                                } catch (Exception e) {
                                    System.out.println("Opcção inválida!");
                                    setorSelecionado = null;
                                    sc.nextLine();
                                }
                            } while (setorSelecionado == null);
                            setorSelecionado.removerFuncionario(funcionarioEditar);
                            funcionarioEditar.setSetor(setorSelecionado);
                            setorSelecionado.adicionarFuncionario(funcionarioEditar);
                            System.out.println("Setor atualizado com sucesso!");
                            break;
                        case 5:
                            double salarioBase = -1;
                            
                            do {
                                try {
                                    System.out.print("Insira o novo salário base = ");
                                    double novoSalario = sc.nextDouble();
                                    try {
                                        funcionarioEditar.getSalario().setSalario(novoSalario);
                                    } catch (FuncionarioException e) {
                                        e.SalarioException(funcionarioEditar);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Tipo inserido inválido. Digite um valor real!");
                                    salarioBase = -1;
                                    sc.nextLine();
                                }
                            } while (salarioBase < 0);
       
                            System.out.println("Setor atualizado com sucesso!");
                            break;
                        case 0:
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Digite um número.");
                    opc = -1; // Força repetição do menu
                    sc.nextLine();
                }
            } while (opc != 0);
        } else {
            System.out.println("Funcionário não encontrado!");
        }
    }

    public void excluirFuncionario(Scanner sc, ArrayList<Funcionario> funcionarios) {
        String cpfExcluir = sc.next();
        Funcionario funcRemover = null;
        for (Funcionario func : funcionarios) {
            if (func.getCpf().equals(cpfExcluir)) {
                funcRemover = func;
                break;
            }
        }
        if (funcRemover != null) {
            funcionarios.remove(funcRemover);
            funcRemover.getSetor().getFuncionarios().remove(funcRemover);
            System.out.println("Funcionário removido com sucesso!");
        } else {
            System.out.println("Funcionário com CPF " + cpfExcluir + " não encontrado.");
        }
    }

}
