import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Bem vindo ao programa!");
        apresentarMenu();
    }

    public static void apresentarMenu() {
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

        char opcaoInicialUsuario = scanner.next().charAt(0);
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
        }

    }

    public static void apresentarMenuFuncionarios(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("-----------------------------------------");
        System.out.println("| 1 - Cadastrar funcionário             |");
        System.out.println("| 2 - Listar funcionários (por setor)   |");
        System.out.println("| 3 - Excluir funcionário               |");
        System.out.println("| 4 - Visualizar total de funcionários  |");
        System.out.println("-----------------------------------------");
        System.out.println();
        int opcaoFuncionarioUsuario = scanner.nextInt();
        Funcionario funcionario = new Funcionario();

        switch (opcaoFuncionarioUsuario) {
            case 1:
        }
    }

    public static void apresentarMenuSalariosBeneficios(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("-------------------------------------------");
        System.out.println("| 5 - Apresentar salário bruto          |");
        System.out.println("| 6 - Calcular salário líquido          |");
        System.out.println("| 7 - Consultar valores dos benefícios  |");
        System.out.println("| 8 - Atualizar valores dos benefícios  |");
        System.out.println("| 9 - Exibir demonstrativo salarial     |");
        System.out.println("------------------------------------------");
        System.out.println();
        int opcaoFuncionarioSalario = scanner.nextInt();
        Salario salario = new Salario();

        switch (opcaoFuncionarioSalario) {
            case 1:
        }
    }

    public static void apresentarMenuProduto(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("---------------------------------------------");
        System.out.println("| 10 - Cadastrar  produto                    |");
        System.out.println("| 11 - Listar produtos                       |");
        System.out.println("| 12 - Atualizar produtos                    |");
        System.out.println("| 13 - Verificar disponibilidade do produto  |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoFuncionarioProduto = scanner.nextInt();
        Produto produto = new Produto();

        switch (opcaoFuncionarioProduto) {
            case 1:
        }
    }

    public static void apresentarMenuCaixa(Scanner scanner) {
        System.out.println("Escolha uma das opções: ");
        System.out.println("______________________________________________");
        System.out.println("| 14 - Registrar entrada                    |");
        System.out.println("| 15 - Registrar saída                      |");
        System.out.println("| 16 - Visualizar saldo atual               |");
        System.out.println("| 17 - Estimar lucro mensal                 |");
        System.out.println("| 18 - Estimar lucro anual                  |");
        System.out.println("| 19 - Gerar relatório                      |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoFuncionarioCaixa = scanner.nextInt();
        Produto produto = new Produto();

        switch (opcaoFuncionarioCaixa) {
            case 1:
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
        int opcaoFuncionarioTransportadora= scanner.nextInt();
        Transportadora transportadora = new Transportadora();

        switch (opcaoFuncionarioTransportadora) {
            case 1:
        }
    }

    public static void apresentarMenuGestao(Scanner scanner){
        System.out.println("Escolha uma das opções: ");
        System.out.println("_____________________________________________");
        System.out.println("| 24 - Consultar negócios em andamento      |");
        System.out.println("| 25 - Atualizar status                     |");
        System.out.println("---------------------------------------------");
        System.out.println();
        int opcaoFuncionarioGestao = scanner.nextInt();
        Funcionario funcionario = new Funcionario();

        switch (opcaoFuncionarioGestao) {
            case 1:
        }
    }


}
