import Enums.Setores;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Funcionario> funcionarios = new ArrayList<>();
    static ArrayList<Setor> setores = new ArrayList<>();
    static ArrayList<Produto> produtos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Bem vindo ao programa!");
        apresentarMenu();
    }

    public static void apresentarMenu() {
        char opcaoInicialUsuario;
        do{
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
                    apresentarMenuSalariosBeneficios(scanner);
                    break;
                case 'P':
                    apresentarMenuProduto(scanner);
                    break;
                case 'C':
                    apresentarMenuCaixa(scanner);
                    break;
                case 'T':
                    apresentarMenuTransportadoras(scanner);
                    break;
                case 'E':
                    apresentarMenuTransportadoras(scanner);
                    break;
                default:
                    System.out.println("\nOpção inválida!\n");
                    break;
            }
        }while(opcaoInicialUsuario != 'E');

    }

    public static void apresentarMenuFuncionarios(Scanner scanner) {
        int opcaoUsuarioFuncionario;
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("-----------------------------------------");
            System.out.println("| 1 - Cadastrar funcionário             |");
            System.out.println("| 2 - Listar funcionários (por setor)   |");
            System.out.println("| 3 - Excluir funcionário               |");
            System.out.println("| 4 - Visualizar total de funcionários  |");
            System.out.println("| 0 - Sair                              |");
            System.out.println("-----------------------------------------");
            System.out.println();
            opcaoUsuarioFuncionario = scanner.nextInt();

            switch (opcaoUsuarioFuncionario) {
                case 1:
                    System.out.printf("Nome = ");
                    String nome = scanner.next();
                    System.out.printf("CPF = ");
                    int cpf = scanner.nextInt();
                    System.out.printf("Gênero = ");
                    String genero = scanner.next();
                    System.out.printf("Setor = ");
                    Setor selecionado = new Setor();
                    for (Setor set : setores) {
                        if (set.getNome().equals(Setores.FINANCEIRO)) {
                            selecionado = set;
                        }
                    }
                    Funcionario funcionario = new Funcionario(nome, cpf, genero, selecionado, null);
                    System.out.println("Salário = ");
                    double salarioFuncionario = scanner.nextDouble();
                    Salario salario = new Salario(salarioFuncionario, funcionario);
                    funcionario.setSalario(salario);
                    funcionarios.add(funcionario);

                    break;

                case 2:
                    break;
                case 3:
                    for (Funcionario func : funcionarios) {
                        System.out.println("Deseja excluir esse funcionário? true or false");
                        System.out.println();
                        boolean excluirFuncionario = scanner.nextBoolean();
                        if (excluirFuncionario) {
                            funcionarios.remove(func);
                        }
                    }
                    break;

                case 4:
                    int totalFuncionarios = 0;
                    for (Funcionario func : funcionarios) {
                        totalFuncionarios++;
                    }
                    System.out.println("Total de funcionários = " + totalFuncionarios);
                    break;
                case 0:
                    System.out.println("Retornando ao menu principal.");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoUsuarioFuncionario != 0);
    }

    public static void apresentarMenuSalariosBeneficios(Scanner scanner) {
        int opcaoUsuarioSalario;
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("-------------------------------------------");
            System.out.println("| 1 - Apresentar salário bruto          |");
            System.out.println("| 2 - Calcular salário líquido          |");
            System.out.println("| 3 - Consultar valores dos benefícios  |");
            System.out.println("| 4 - Atualizar valores dos benefícios  |");
            System.out.println("| 5 - Exibir demonstrativo salarial     |");
            System.out.println("| 0 - Sair                              |");
            System.out.println("------------------------------------------");
            System.out.println();
            opcaoUsuarioSalario = scanner.nextInt();

            switch (opcaoUsuarioSalario) {
                case 1:

            }
        }
        while (opcaoUsuarioSalario != 0);
    }

    public static void apresentarMenuProduto(Scanner scanner) {
        int opcaoUsuarioProduto;
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("---------------------------------------------");
            System.out.println("| 1 - Cadastrar  produto                    |");
            System.out.println("| 2 - Listar produtos                       |");
            System.out.println("| 3 - Atualizar produtos                    |");
            System.out.println("| 4 - Verificar disponibilidade do produto  |");
            System.out.println("| 0  - Sair                                 |");
            System.out.println("---------------------------------------------");
            System.out.println();
            opcaoUsuarioProduto = scanner.nextInt();

            switch (opcaoUsuarioProduto) {
                case 1:
                    System.out.printf("Descrição = ");
                    String descricao = scanner.nextLine();
                    System.out.printf("Valor de venda = ");
                    double valorVenda = scanner.nextDouble();
                    System.out.printf("Valor de compra = ");
                    double valorCompra = scanner.nextDouble();
                    Produto produto = new Produto(descricao, valorVenda, valorCompra);
                    produtos.add(produto);
                    break;

                case 2:
                    for (Produto prod : produtos) {
                        System.out.println(prod);
                        System.out.println();
                    }
            }
        } while (opcaoUsuarioProduto != 0);

    }

    public static void apresentarMenuCaixa(Scanner scanner) {
        int opcaoUsuarioCaixa;

        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("______________________________________________");
            System.out.println("| 1 - Registrar entrada                    |");
            System.out.println("| 2 - Registrar saída                      |");
            System.out.println("| 3 - Visualizar saldo atual               |");
            System.out.println("| 4 - Verificar lucro mensal               |");
            System.out.println("| 5 - Verificar lucro anual                |");
            System.out.println("| 6 - Gerar relatório                      |");
            System.out.println("| 0 - Sair                                 |");
            System.out.println("---------------------------------------------");
            System.out.println();
            opcaoUsuarioCaixa = scanner.nextInt();
            Caixa caixa = new Caixa();

            switch (opcaoUsuarioCaixa) {
                case 16:
                    System.out.println("Saldo atual da conta = " + caixa.getSaldo());
                    break;

                case 17:
                    System.out.printf("Mes = ");
                    int mes = scanner.nextInt();
                    System.out.println();
                    System.out.printf("Ano = ");
                    int ano = scanner.nextInt();
                    caixa.lucroMensal(mes, ano);
                    break;

                case 18:
                    System.out.printf("Ano = ");
                    ano = scanner.nextInt();
                    caixa.lucroAnual(ano);
            }
        } while (opcaoUsuarioCaixa != 0);
    }

    public static void apresentarMenuTransportadoras(Scanner scanner) {
        int opcaoUsuarioTransportadoras;
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("______________________________________________");
            System.out.println("| 1 - Cadastrar                            |");
            System.out.println("| 2 - Listar                               |");
            System.out.println("| 3 - Atualizar                            |");
            System.out.println("| 4 - Visualizar total                     |");
            System.out.println("| 0 - Sair                                 |");
            System.out.println("---------------------------------------------");
            System.out.println();
            opcaoUsuarioTransportadoras = scanner.nextInt();

            switch (opcaoUsuarioTransportadoras) {
                case 1:
                    System.out.printf("Nome = ");
                    String nome = scanner.nextLine();
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
