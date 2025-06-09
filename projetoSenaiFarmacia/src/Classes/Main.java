package Classes;
import Enums.Regiao;
import Enums.Setores;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import Gerenciadores.*;

public class Main {
    Scanner scanner = new Scanner(System.in);
    static ArrayList<Funcionario> funcionarios = new ArrayList<>();
    static ArrayList<Setor> setores = new ArrayList<>();
    static ArrayList<Produto> produtos = new ArrayList<>();
    static ArrayList<Transportadora> transportadoras = new ArrayList<>();
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
                    apresentarMenuTransportadoras(scanner, transportadoras);
                    break;
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
            new GerFuncionario().menu();
            opcaoUsuarioFuncionario = scanner.nextInt();

            switch (opcaoUsuarioFuncionario) {
                case 1:
                    new GerFuncionario().cadastrarFuncionario(setores, funcionarios);
                    break;

                case 2:
                    new GerFuncionario().listarFuncionariosPorSetor(setores);
                    break;

                case 3:
                    System.out.print("Informe o ID do funcionário: ");
                    String idFuncionario = scanner.next();
                    Funcionario funcionarioEditar = new GerFuncionario().buscarFuncionarioPorId(idFuncionario, funcionarios);
                    new GerFuncionario().editarFuncionario(funcionarioEditar, setores);
                    break;

                case 4:
                    System.out.print("Informe o CPF do funcionário a ser removido: ");
                    new GerFuncionario().excluirFuncionario(funcionarios);
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
            opcaoUsuarioSalario = scanner.nextInt();

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

    public static Funcionario buscarFuncionarioPorId(String id, ArrayList<Funcionario> funcionarios) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId().equals(id)) {
                return funcionario;
            }
        }
        return null;
    }



    public static void apresentarMenuProduto(Scanner scanner) {
        int opcaoUsuarioProduto;
        do {
            new GerProduto().menu();
            opcaoUsuarioProduto = scanner.nextInt();

            switch (opcaoUsuarioProduto) {
                case 1:
                    new GerProduto().cadastraProduto(produtos);
                    break;

                case 2:
                    new GerProduto().listarProdutos(produtos);
                    scanner.nextLine();
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
            opcaoUsuarioCaixa = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoUsuarioCaixa) {
                case 1:
                    Venda novaVenda = new Venda();
                    new GerCaixa().registrarEntrada(caixa, novaVenda, funcionarios, transportadoras, setores, produtos);
                    break;

                case 2:
                    Compra novaCompra = realizarCompra(scanner);
                    caixa.getSaida().add(novaCompra);
                    System.out.println("Compra registrada com sucesso!");
                    break;

                case 3:
                    System.out.println("Saldo atual: " + caixa.getSaldo());
                    break;

                case 4:
                    System.out.println("Digite o mês (1-12): ");
                    int mes = scanner.nextInt();

                    System.out.println("Digite o ano: ");
                    int ano = scanner.nextInt();

                    double lucroMensal = caixa.lucroMensal(mes, ano);
                    System.out.println("Lucro do mês: " + lucroMensal);
                    break;

                case 5:
                    System.out.println("Digite o ano: ");
                    int anoAnual = scanner.nextInt();

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

    public static Compra realizarCompra(Scanner scanner){
        int prod, quantidade;    
        Compra compra = new Compra();
        Funcionario funcionarioCompra = null;
        ArrayList<Itens> itens = new ArrayList<>();

        System.out.println("Iniciando nova compra...");
        
        
        do{
            System.out.println("Insira o id do produto ou 0 para parar: ");
            prod = scanner.nextInt();
            
            if (prod != 0 ) {
                Produto produto = buscarProdutoPorId(prod);
            
                System.out.println("Quantas unidades do produto "+ produto.getDescricao() +" deseja adicionar? (0 para cancelar)");
                quantidade = scanner.nextInt();

                if (quantidade != 0 ) {
                    Itens item = new Itens(quantidade, produto);
                    itens.add(item);
                }
            }
        }while(false);

        if (itens.size() > 0) {
            compra.setProdutos(itens);

            do {
                System.out.print("Informe o ID do funcionário: ");
                String idFuncionario = scanner.next();
                funcionarioCompra = buscarFuncionarioPorId(idFuncionario, funcionarios);

                if (funcionarioCompra != null) {
                    compra.setFuncionario(funcionarioCompra);
                }else {
                    System.out.println("\nFuncionário não encontrado !\n");
                }
            } while (funcionarioCompra == null);
            

            System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
            String dataCompra = scanner.nextLine();
            LocalDate dtCompra = LocalDate.parse(dataCompra);
            if (dataCompra.equals("HJ")) {
                dtCompra = LocalDate.now();
            }else {
                dtCompra = LocalDate.parse(dataCompra);
            }
            compra.setData(dtCompra);

            if (dtCompra.isAfter(LocalDate.now())) {
                compra.setStatus(Enums.Status.ABERTO);
            }else {
                compra.setStatus(Enums.Status.FECHADO);;
            }

            compra.setValor(compra.calculaTotal());

            return compra;
        }else{
            System.out.println("Nenhum produto foi definido!");

            return null;
        }
        
    }

    public static Produto buscarProdutoPorId(int id){
        for(Produto produto : produtos){
            if (produto.getId().equals(id)) {
                return produto;
            }
        }
        return null;
    }

    public static void apresentarMenuTransportadoras(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        int opcaoUsuarioTransportadoras;

        do {
            new GerTransportadora().menu();
            opcaoUsuarioTransportadoras = scanner.nextInt();
            scanner.nextLine();

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
        int opcaoUsuarioGestao;
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("_____________________________________________");
            System.out.println("| 1 - Consultar negócios em andamento      |");
            System.out.println("| 2 - Atualizar status                     |");
            System.out.println("| 0 - Sair                                 |");
            System.out.println("---------------------------------------------");
            System.out.println();
            opcaoUsuarioGestao = scanner.nextInt();

            switch (opcaoUsuarioGestao) {
                case 1:
            }
        } while (opcaoUsuarioGestao != 0);
    }
}