package Classes;
import Enums.Setores;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Gerenciadores.*;

public class Main {
    Scanner scanner = new Scanner(System.in);
    static ArrayList<Funcionario> funcionarios = new ArrayList<>();
    static ArrayList<Setor> setores = new ArrayList<>();
    static ArrayList<Produto> produtos = new ArrayList<>();
    static Caixa caixa = new Caixa();

    static {
        setores.add(new Setor(Setores.ALMOXARIFADO));
        setores.add(new Setor(Setores.ATENDIMENTO_CLIENTE));
        setores.add(new Setor(Setores.FINANCEIRO));
        setores.add(new Setor(Setores.GERENTE_FILIAL));
        setores.add(new Setor(Setores.GESTAO_PESSOAS));
        setores.add(new Setor(Setores.VENDAS));
    }

    public static void main(String[] args) {
        System.out.println("Bem vindo ao programa!");
        apresentarMenu();
    }

    public static void apresentarMenu() {
        char opcaoInicialUsuario;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("ESCOLHA UMA DAS OPÇÕES: ");
            System.out.println();
            System.out.println("FUNCIONÁRIOS - (F)");
            System.out.println("SALÁRIO E BENEFÍCIOS - (S)");
            System.out.println("PRODUTO - (P)");
            System.out.println("CAIXA - (C)");
            System.out.println("TRANSPORTADORAS - (T)");
            System.out.println("GESTÃO DE NEGÓCIOS - (G)");
            System.out.println();
            System.out.println("SAIR - (E)");
            System.out.println();

            opcaoInicialUsuario = scanner.next().toUpperCase().charAt(0);

            switch (opcaoInicialUsuario) {
                case 'F':
                    apresentarMenuFuncionarios(scanner);
                    break;
                case 'S':
                    apresentarMenuSalarios(scanner, funcionarios);
                    break;
                case 'P':
                    apresentarMenuProduto(scanner);
                    break;
                case 'C':
                    apresentarMenuCaixa(scanner);
                    break;
                case 'T':
                    apresentarMenuTransportadoras(scanner, Transportadora.getTransportadoras());
                    break;
                case 'G':
                    apresentarMenuGestao(scanner);
                case 'E':
                    System.out.println("Obrigado por utilizar o nosso sistema!");
                    break;
                default:
                    System.out.println("\nOpção inválida!\n");
                    break;
            }
        } while (opcaoInicialUsuario != 'E');

    }

    public static void apresentarMenuFuncionarios(Scanner scanner) {
        int opcaoUsuarioFuncionario;
        do {
            try {
                new GerFuncionario().menu();
                opcaoUsuarioFuncionario = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){
                scanner.nextLine();
                opcaoUsuarioFuncionario = 6;
            }

            switch (opcaoUsuarioFuncionario) {
                case 1:
                    new GerFuncionario().cadastrarFuncionario(scanner, setores, funcionarios);
                    break;

                case 2:
                    new GerFuncionario().listarFuncionariosPorSetor(scanner, setores);
                    break;

                case 3:
                    Funcionario funcionarioEditar = null;
                    
                    do{
                        System.out.print("Informe o ID do funcionário: ");
                        String idFuncionario = scanner.next();
                        funcionarioEditar = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);
                        if (funcionarioEditar == null) System.out.println("Funcionário não encontrado!");
                    }while(funcionarioEditar == null);
                    
                    new GerFuncionario().editarFuncionario(scanner, funcionarioEditar, setores);
                    break;

                case 4:
                    System.out.print("Informe o CPF do funcionário a ser removido: ");
                    new GerFuncionario().excluirFuncionario(scanner, funcionarios);
                    break;

                case 5:
                    int totalFuncionarios = Setor.totalFuncionarios(setores);
                    System.out.println("Total de funcionários cadastrados: " + totalFuncionarios);
                    break;

                case 0:
                    System.out.println("Retornando ao menu principal.");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        } while (opcaoUsuarioFuncionario != 0);
    }

    public static void apresentarMenuSalarios(Scanner scanner, ArrayList<Funcionario> funcionarios) {
        int opcaoUsuarioSalario;

        do {
            new GerSalario().menu();

            try {
                opcaoUsuarioSalario = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException inputExcpt){
                scanner.nextLine();
                opcaoUsuarioSalario = 6;
            }

            switch (opcaoUsuarioSalario) {
                case 1:
                    new GerSalario().salarioBruto(funcionarios);
                    break;
                case 2:

                    new GerSalario().calcularSalario(funcionarios);
                    break;

                case 3:
                    new GerSalario().consultarBeneficios(funcionarios);
                    break;

                case 4:
                    new GerSalario().exibirDemonstrativo(funcionarios);
                    break;

                case 5:
                    new GerSalario().valorBonificacao(caixa, setores);
                    break;

                case 0:
                    System.out.println("Retornando ao menu principal.");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoUsuarioSalario != 0);
    }

    public static void apresentarMenuProduto(Scanner scanner) {
        int opcaoUsuarioProduto;
        do {
            new GerProduto().menu();
            try {
                opcaoUsuarioProduto = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException inputExcpt){
                scanner.nextLine();
                opcaoUsuarioProduto = 6;
            }

            switch (opcaoUsuarioProduto) {
                case 1:
                    new GerProduto().cadastraProduto(produtos);
                    break;

                case 2:
                    new GerProduto().listarProdutos(produtos);
                    break;

                case 3:
                    new GerProduto().atualizarProduto(produtos);
                    break;

                case 4:
                    new GerProduto().diponibilidadeProduto(produtos);
                    break;

                case 0:
                    System.out.println("Retornando ao menu principal.");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoUsuarioProduto != 0);
    }

    public static void apresentarMenuCaixa(Scanner scanner) {
        int opcaoUsuarioCaixa;

        do {
            new GerCaixa().menu();
            try {
                opcaoUsuarioCaixa = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException inputExcpt){
                scanner.nextLine();
                opcaoUsuarioCaixa = 6;
            }

            switch (opcaoUsuarioCaixa) {
                case 1:
                    Venda novaVenda = new Venda();
                    new GerCaixa().registrarEntrada(caixa, novaVenda, funcionarios, Transportadora.getTransportadoras(), setores, produtos);
                    break;

                case 2:
                    Compra novaCompra = new Compra();
                    new GerCaixa().registrarSaida(caixa, novaCompra, funcionarios, produtos);
                    break;

                case 3:
                    System.out.println("Saldo atual: " + caixa.getSaldo());
                    break;

                case 4:
                    int mes = 0;
                    int ano = 0;

                    do {
                        try {
                            System.out.println("Digite o mês (1-12): ");
                            mes = scanner.nextInt();
                            scanner.nextLine();                  
                        } catch (Exception e) {
                            System.out.println("Tipo inserido inválido!");    
                        }
                    } while (mes < 1 || mes > 12);

                    do {
                        try {
                            System.out.println("Digite o ano: ");
                            ano = scanner.nextInt();
                            scanner.nextLine();                     
                        } catch (Exception e) {
                            System.out.println("Tipo inserido inválido!"); 
                        }
                    } while (ano < 2000 || ano > 2050);

                    

                    double lucroMensal = caixa.lucroMensal(mes, ano);
                    System.out.println("Lucro do mês: " + lucroMensal);
                    break;

                case 5:
                    int anoAnual = 0;
                    do {
                        try {
                            System.out.println("Digite o ano: ");
                            anoAnual = scanner.nextInt();
                            scanner.nextLine();                 
                        } catch (Exception e) {
                            System.out.println("Tipo inserido inválido!");     
                        }
                    } while (anoAnual < 2000 || anoAnual > 2050);

                    double lucroAnual = caixa.lucroAnual(anoAnual);
                    System.out.println("Lucro do ano: " + lucroAnual);
                    break;

                case 6:
                    System.out.println("Relatório de transações:");
                    System.out.println("Entradas:");
                    for (Venda v : caixa.getEntrada()) {
                        System.out.println("Valor: " + v.getValor() + ", Data: " + v.getData());
                    }
                    System.out.println("Saídas:");
                    for (Compra c : caixa.getSaida()) {
                        System.out.println("Valor: " + c.getValor() + ", Data: " + c.getData());
                    }
                    break;
                case 0:
                    System.out.println("Saindo do menu do caixa.");
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        } while (opcaoUsuarioCaixa != 0);
    }

    public static void apresentarMenuTransportadoras(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        int opcaoUsuarioTransportadoras;

        do {
            new GerTransportadora().menu();
            try {
                opcaoUsuarioTransportadoras = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException inputExcpt){
                opcaoUsuarioTransportadoras = 6;
            }

            switch (opcaoUsuarioTransportadoras) {
                case 1:
                    new GerTransportadora().cadastrarTransportadora(scanner, transportadoras);
                    break;

                case 2:
                    GerTransportadora.listarTransportadoras(transportadoras);
                    break;

                case 3:
                    GerTransportadora.atualizarTransportadora(scanner, transportadoras);
                    break;

                case 4:
                    GerTransportadora.visualizarTotalTransportadoras(transportadoras);
                    break;

                case 0:
                    System.out.println("Saindo do menu de transportadoras.");
                    break;

                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        } while (opcaoUsuarioTransportadoras != 0);
    }

   public static void apresentarMenuGestao(Scanner scanner) {
        GerGestao gerGestao = new GerGestao(caixa.getSaida(), caixa.getEntrada());
        gerGestao.apresentarMenuGestao(scanner, caixa);
    }
}