import Enums.Setores;

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
        Scanner scanner = new Scanner(System.in);
        char opcaoInicialUsuario;

        do {
            System.out.println();
            System.out.println("ESCOLHA UMA DAS OPÇÕES: ");
            System.out.println();
            System.out.println("FUNCIONÁRIOS - (F)");
            System.out.println("SALÁRIO E BENEFÍCIOS - (S)");
            System.out.println("PRODUTO - (P)");
            System.out.println("CAIXA - (C)");
            System.out.println("TRANSPORTADORAS - (T)");
            System.out.println("GESTÃO DE NEGÓCIOS - (G)");
            System.out.println("SAIR - (0)");
            System.out.println();

            opcaoInicialUsuario = scanner.next().charAt(0);
            opcaoInicialUsuario = Character.toUpperCase(opcaoInicialUsuario);

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
                case 'G':
                    apresentarMenuGestao(scanner);
                    break;
                case '0':
                    System.out.println("Saindo do programa.");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoInicialUsuario != '0');

    }

    public static void apresentarMenuFuncionarios(Scanner scanner) {
        int opcaoUsuarioFuncionario = scanner.nextInt();
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
        int opcaoUsuarioSalario = scanner.nextInt();
        do {
            System.out.println("Escolha uma das opções: ");
            System.out.println("-------------------------------------------");
            System.out.println("| 5 - Apresentar salário bruto          |");
            System.out.println("| 6 - Calcular salário líquido          |");
            System.out.println("| 7 - Consultar valores dos benefícios  |");
            System.out.println("| 8 - Atualizar valores dos benefícios  |");
            System.out.println("| 9 - Exibir demonstrativo salarial     |");
            System.out.println("------------------------------------------");
            System.out.println();

            Salario salario = new Salario();

            switch (opcaoUsuarioSalario) {
                case 1:

            }
        }

        public static void apresentarMenuProduto (Scanner scanner){
            System.out.println("Escolha uma das opções: ");
            System.out.println("---------------------------------------------");
            System.out.println("| 10 - Cadastrar  produto                    |");
            System.out.println("| 11 - Listar produtos                       |");
            System.out.println("| 12 - Atualizar produtos                    |");
            System.out.println("| 13 - Verificar disponibilidade do produto  |");
            System.out.println("---------------------------------------------");
            System.out.println();
            int opcaoUsuarioProduto = scanner.nextInt();

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
        }
        while (opcaoUsuarioSalario) ;
    }

    public static void apresentarMenuCaixa(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("______________________________________________");
        System.out.println("| 14 - Registrar entrada                    |");
        System.out.println("| 15 - Registrar saída                      |");
        System.out.println("| 16 - Visualizar saldo atual               |");
        System.out.println("| 17 - Verificar lucro mensal               |");
        System.out.println("| 18 - Verificar lucro anual                |");
        System.out.println("| 19 - Gerar relatório                      |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoUsuarioCaixa = scanner.nextInt();
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
    }

    public static void apresentarMenuTransportadoras(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("______________________________________________");
        System.out.println("| 20 - Cadastrar                            |");
        System.out.println("| 21 - Listar                               |");
        System.out.println("| 22 - Atualizar                            |");
        System.out.println("| 23 - Visualizar total                     |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoUsuarioTransportadora = scanner.nextInt();

        switch (opcaoUsuarioTransportadora) {
            case 1:
                System.out.printf("Nome = ");
                String nome = scanner.nextLine();
        }
    }

    public static void apresentarMenuGestao(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("_____________________________________________");
        System.out.println("| 24 - Consultar negócios em andamento      |");
        System.out.println("| 25 - Atualizar status                     |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoUsuarioGestao = scanner.nextInt();

        switch (opcaoUsuarioGestao) {
            case 1:
        }
    }
}
