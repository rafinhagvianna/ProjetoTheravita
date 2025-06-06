import Enums.Regiao;
import Enums.Setores;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;

public class Main {
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
            GenFuncionario.menu();
            opcaoUsuarioFuncionario = scanner.nextInt();

            switch (opcaoUsuarioFuncionario) {
                case 1:
                    GenFuncionario.cadastrarFuncionario(setores);
                    break;

                case 2:
                    GenFuncionario.listarFuncionariosPorSetor(setores);
                    break;

                case 3:
                    System.out.print("Informe o ID do funcionário: ");
                    String idFuncionario = scanner.next();
                    Funcionario funcionarioEditar = buscarFuncionarioPorId(idFuncionario, funcionarios);
                    GenFuncionario.editarFuncionario(funcionarioEditar, setores);
                    break;

                case 4:
                    System.out.print("Informe o CPF do funcionário a ser removido: ");
                    GenFuncionario.excluirFuncionario(funcionarios);
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

            opcaoUsuarioSalario = scanner.nextInt();

            switch (opcaoUsuarioSalario) {
                case 1:

                    System.out.print("Informe o ID do funcionário: ");
                    String idFuncionario = scanner.next();
                    Funcionario funcionarioBruto = buscarFuncionarioPorId(idFuncionario, funcionarios);

                    if (funcionarioBruto != null && funcionarioBruto.getSalario() != null) {
                        System.out.println("Salário bruto de " + funcionarioBruto.getNome() + ": R$ " + funcionarioBruto.getSalario().getSalario());
                    } else {
                        System.out.println("Funcionário ou salário não encontrado.");
                    }
                    break;

                case 2:

                    System.out.print("Informe o ID do funcionário: ");
                    idFuncionario = scanner.next();
                    Funcionario funcionarioLiquido = buscarFuncionarioPorId(idFuncionario, funcionarios);

                    if (funcionarioLiquido != null && funcionarioLiquido.getSalario() != null) {
                        double salarioLiquido = funcionarioLiquido.getSalario().calculaSalario();
                        System.out.println("Salário líquido de " + funcionarioLiquido.getNome() + ": R$ " + salarioLiquido);
                    } else {
                        System.out.println("Funcionário ou salário não encontrado.");
                    }
                    break;

                case 3:

                    System.out.print("Informe o ID do funcionário: ");
                    idFuncionario = scanner.next();
                    Funcionario funcionarioBeneficios = buscarFuncionarioPorId(idFuncionario, funcionarios);

                    if (funcionarioBeneficios != null && funcionarioBeneficios.getSalario() != null) {
                        Salario salarioFuncionario = funcionarioBeneficios.getSalario();
                        System.out.println("Benefícios de " + funcionarioBeneficios.getNome() + ":");
                        System.out.println(" - Plano de saúde: R$ " + salarioFuncionario.getSaude());
                        System.out.println(" - Vale refeição/alimentação: R$ " + salarioFuncionario.getValeRefAliment());
                        System.out.println(" - Plano odontológico: R$ " + salarioFuncionario.getOdonto());
                    } else {
                        System.out.println("Funcionário ou salário não encontrado.");
                    }
                    break;

                case 4:

                    System.out.print("Informe o ID do funcionário: ");
                    idFuncionario = scanner.next();
                    Funcionario funcionarioDemonstrativo = buscarFuncionarioPorId(idFuncionario, funcionarios);

                    if (funcionarioDemonstrativo != null) {
                        funcionarioDemonstrativo.dadosFuncionario();
                    } else {
                        System.out.println("Funcionário não encontrado.");
                    }
                    break;

                case 5:
                    if (caixa != null && setores != null) {
                        System.out.println("\nBonificação por funcionário: " + Salario.calcularBonificacao(caixa, setores));
                    } else {
                        System.out.println("Não há registro de caixa ou setor");
                    }
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
            System.out.println("Escolha uma das opções: ");
            System.out.println("---------------------------------------------");
            System.out.println("| 1 - Cadastrar produto                     |");
            System.out.println("| 2 - Listar produtos                       |");
            System.out.println("| 3 - Atualizar produtos                    |");
            System.out.println("| 4 - Verificar disponibilidade do produto  |");
            System.out.println("| 0  - Sair                                 |");
            System.out.println("---------------------------------------------");
            opcaoUsuarioProduto = scanner.nextInt();

            switch (opcaoUsuarioProduto) {
                case 1:
                    GenProduto.cadastraProduto(produtos);
                    break;

                case 2:
                    GenProduto.listarProdutos(produtos);
                    scanner.nextLine();
                    break;

                case 3:
                    GenProduto.atualizarProduto(produtos);
                    break;

                case 4:
                    GenProduto.diponibilidadeProduto(produtos);
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
            scanner.nextLine();

            switch (opcaoUsuarioCaixa) {
                case 1:

                    Venda novaVenda = realizarVenda(scanner);
                    caixa.getEntrada().add(novaVenda);
                    System.out.println("Venda registrada com sucesso!");
                    
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

    public static Venda realizarVenda(Scanner scanner){
        int prod, quantidade;    
        Venda venda = new Venda();
        Funcionario funcionarioVenda = null;
        Regiao regiao;
        Transportadora transportadora = null;
        ArrayList<Itens> itens = new ArrayList<>();

        System.out.println("Iniciando nova venda...");
        
        
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
            venda.setProdutos(itens);

            do {
                System.out.print("Informe o ID do funcionário: ");
                String idFuncionario = scanner.next();
                funcionarioVenda = buscarFuncionarioPorId(idFuncionario, funcionarios);

                if (funcionarioVenda != null) {
                    venda.setFuncionario(funcionarioVenda);
                }else {
                    System.out.println("\nFuncionário não encontrado !\n");
                }
            } while (funcionarioVenda == null);
            

            System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
            String dataVenda = scanner.nextLine();
            LocalDate dtVenda = LocalDate.parse(dataVenda);
            if (dataVenda.equals("HJ")) {
                dtVenda = LocalDate.now();
            }else {
                dtVenda = LocalDate.parse(dataVenda);
            }
            venda.setData(dtVenda);

            if (dtVenda.isAfter(LocalDate.now())) {
                venda.setStatus(Enums.Status.ABERTO);
            }else {
                venda.setStatus(Enums.Status.FECHADO);;
            }

            do{
                System.out.println("Escolha a região de entrega da venda:");
                Regiao[] regioes = Regiao.values();
                for (int i = 0; i < regioes.length; i++) {
                    System.out.println((i + 1) + " - " + regioes[i]);
                }
                int regiaoEscolhida = scanner.nextInt();
                try{
                    regiao = regioes[regiaoEscolhida - 1];
                }catch (ArrayIndexOutOfBoundsException | InputMismatchException e){
                    System.out.println("Opção inválida! Tente novamente.");
                    scanner.nextLine();
                    regiao = null;
                }
            }while(regiao != null);

            do {
                System.out.println("Escolha a transportadora para a venda:");
                for (int i = 0; i < transportadoras.size(); i++) {
                    System.out.println((i + 1) + " - " + setores.get(i).getNome());
                }
                int transportadoraEscolhida = scanner.nextInt();

                try{
                    transportadora = transportadoras.get(transportadoraEscolhida);
                }catch (ArrayIndexOutOfBoundsException | InputMismatchException e){
                    System.out.println("Opção inválida! Tente novamente.");
                    scanner.nextLine();
                    transportadora = null;
                }

                
                if (!transportadora.atendeRegiao(regiao) && transportadora != null ) {
                    System.out.println("Transportadora não atende região definida!");
                    transportadora = null;
                }
            } while (transportadora == null);

            venda.setTransportadora(transportadora);

            venda.setValor(venda.calculaTotal());

            return venda;
        }else{
            System.out.println("Nenhum produto foi definido!");

            return null;
        }
        
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
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }



    public static void apresentarMenuTransportadoras(Scanner scanner, ArrayList<Transportadora> transportadoras) {
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
            scanner.nextLine();

            switch (opcaoUsuarioTransportadoras) {
                case 1:
                    genTransportadora.cadastrarTransportadora(scanner, transportadoras);
                    break;

                case 2:
                    genTransportadora.listarTransportadoras(transportadoras);
                    break;

                case 3:
                    genTransportadora.atualizarTransportadora(scanner, transportadoras);
                    break;

                case 4:
                    genTransportadora.visualizarTotalTransportadoras(transportadoras);
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