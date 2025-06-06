import Enums.Setores;
import java.util.ArrayList;
import java.util.Scanner;
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
        }while(opcaoInicialUsuario != 'E');

    }

    public static void apresentarMenuFuncionarios(Scanner scanner) {
        int opcaoUsuarioFuncionario;
        do {
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
            opcaoUsuarioFuncionario = scanner.nextInt();

            switch (opcaoUsuarioFuncionario) {
                case 1:

                    System.out.print("Nome = ");
                    String nome = scanner.next();
                    System.out.print("CPF = ");
                    String cpf = scanner.next();
                    System.out.print("Gênero = ");
                    String genero = scanner.next();


                    System.out.println("Escolha o setor para o funcionário:");
                    for (int i = 0; i < setores.size(); i++) {
                        System.out.println((i + 1) + " - " + setores.get(i).getNome());
                    }
                    int setorEscolhido = scanner.nextInt();
                    Setor setorSelecionado = setores.get(setorEscolhido - 1);


                    System.out.print("Salário base = ");
                    double salarioBase = scanner.nextDouble();


                    Funcionario funcionario = new Funcionario(nome, cpf, genero, setorSelecionado, null);


                    Salario salario = new Salario(salarioBase, funcionario);
                    funcionario.setSalario(salario);


                    setorSelecionado.getFuncionarios().add(funcionario);


                    funcionarios.add(funcionario);
                    System.out.println("Funcionário cadastrado com sucesso!");
                    break;

                case 2:

                    System.out.println("Escolha o setor para o funcionário:");
                    for (int i = 0; i < setores.size(); i++) {
                        System.out.println((i + 1) + " - " + setores.get(i).getNome());
                    }
                    setorEscolhido = scanner.nextInt();
                    setorSelecionado = setores.get(setorEscolhido - 1);

                    System.out.println("Funcionários do setor " + setorSelecionado.getNome() + ":");
                    for (Funcionario func : setorSelecionado.getFuncionarios()) {
                        System.out.println(func.getNome() + " - " + func.getId());
                    }

                    break;

                case 3:
                    System.out.print("Informe o ID do funcionário: ");
                    String idFuncionario = scanner.next();
                    Funcionario funcionarioEditar = buscarFuncionarioPorId(idFuncionario, funcionarios);
                    int opc; 
                    if (funcionarioEditar != null) {
                        do{
                            System.out.print("Qual dado deseja modificar? \n 1- Nome\n2 - CPF\n3 - Genero\n4 - Setor\n5 - Salário\n0 - Sair");
                            opc = scanner.nextInt();

                            switch (opc) {
                                case 1:
                                    System.out.print("Insira o novo nome = ");
                                    funcionarioEditar.setNome(scanner.next());
                                    break;
                                case 2:
                                    System.out.print("Insira o novo CPF = ");
                                    funcionarioEditar.setCpf(scanner.next());
                                    break;
                                case 3:     
                                    System.out.print("Insira o novo gênero = ");
                                    funcionarioEditar.setGenero(scanner.next());
                                    break;
                                case 4:
                                    System.out.println("Escolha o setor para o funcionário:");
                                    for (int i = 0; i < setores.size(); i++) {
                                        System.out.println((i + 1) + " - " + setores.get(i).getNome());
                                    }
                                    setorEscolhido = scanner.nextInt();
                                    funcionarioEditar.setSetor(setores.get(setorEscolhido - 1));
                                    break;
                                case 5:
                                    System.out.print("Insira o novo salário base = ");
                                    funcionarioEditar.getSalario().setSalario(scanner.nextDouble());
                                    break;

                                case 0:
                                break;
                            
                                default:
                                    System.out.println("Opção inválida");
                                    break;
                            }
                        }while (opc != 0);
                    }else {
                        System.out.println("Funcionário não encontrado !");
                    }
                    break;

                case 4:

                    System.out.print("Informe o CPF do funcionário a ser removido: ");
                    String cpfExcluir = scanner.next();
                    Funcionario funcRemover = null;
                    System.out.println("Teste1");
                    for (Funcionario func : funcionarios) {

                        System.out.println(func.getCpf() +"=="+ cpfExcluir);
                        if (func.getCpf().equals(cpfExcluir)) {
                            System.out.println("Teste2");
                            funcRemover = func;
                            break;
                        }
                    }
                    if (funcRemover != null) {
                        funcionarios.remove(funcRemover);
                        funcRemover.getSetor().getFuncionarios().remove(funcRemover);
                        System.out.println("Funcionário removido com sucesso!");
                    } else {
                        System.out.println("Teste3");
                        System.out.println("Funcionário com CPF " + cpfExcluir + " não encontrado.");
                    }
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
                        System.out.println("\nBonificação por funcionário: "+ Salario.calcularBonificacao(caixa, setores));
                    }else {
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
            System.out.println("| 1 - Cadastrar  produto                    |");
            System.out.println("| 2 - Listar produtos                       |");
            System.out.println("| 3 - Atualizar produtos                    |");
            System.out.println("| 4 - Verificar disponibilidade do produto  |");
            System.out.println("| 0  - Sair                                 |");
            System.out.println("---------------------------------------------");
            opcaoUsuarioProduto = scanner.nextInt();

            switch (opcaoUsuarioProduto) {
                case 1:
                    scanner.nextLine();
                    System.out.print("Descrição = ");
                    String descricao = scanner.nextLine();
                    System.out.print("Valor de venda = ");
                    double valorVenda = scanner.nextDouble();
                    System.out.print("Valor de compra = ");
                    double valorCompra = scanner.nextDouble();
                    Produto produto = new Produto(descricao, valorVenda, valorCompra);
                    produtos.add(produto);
                    break;

                case 2:
                    for (Produto prod : produtos) {
                        System.out.println(prod);
                    }
                    break;

                case 3:
                    System.out.print("Informe a descrição do produto a ser atualizado: ");
                    scanner.nextLine();
                    String descricaoProduto = scanner.nextLine();
                    Produto produtoAtualizar = null;
                    for (Produto prod : produtos) {
                        if (prod.getDescricao().equalsIgnoreCase(descricaoProduto)) {
                            produtoAtualizar = prod;
                            break;
                        }
                    }
                    if (produtoAtualizar != null) {
                        System.out.print("Novo valor de venda: ");
                        double novoValorVenda = scanner.nextDouble();
                        produtoAtualizar.setValorVenda(novoValorVenda);
                        System.out.println("Produto atualizado com sucesso!");
                    } else {
                        System.out.println("Produto não encontrado!");
                    }
                    break;

                case 4:
                    System.out.print("Informe a descrição do produto para verificar disponibilidade: ");
                    scanner.nextLine();
                    String produtoDescricao = scanner.nextLine();
                    boolean encontrado = false;
                    for (Produto prod : produtos) {
                        if (prod.getDescricao().equalsIgnoreCase(produtoDescricao)) {
                            System.out.println("Produto disponível: " + prod);
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        System.out.println("Produto não encontrado!");
                    }
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
                    System.out.println("Digite o valor da venda: ");
                    double valorVenda = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Digite a data da venda (AAAA-MM-DD): ");
                    String dataVenda = scanner.nextLine();


                    Venda novaVenda = new Venda(valorVenda, LocalDate.parse(dataVenda));
                    caixa.getEntrada().add(novaVenda);
                    System.out.println("Venda registrada com sucesso!");
                    break;

                case 2:
                    System.out.println("Digite o valor da compra: ");
                    double valorCompra = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Digite a data da compra (AAAA-MM-DD): ");
                    String dataCompra = scanner.nextLine();


                    Compra novaCompra = new Compra();
                    novaCompra.setValor(valorCompra);
                    novaCompra.setData(LocalDate.parse(dataCompra));


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

                    System.out.println("Digite o nome da transportadora: ");
                    String nome = scanner.nextLine();

                    System.out.println("Digite o ID da transportadora: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Digite o CNPJ da transportadora: ");
                    String cnpj = scanner.nextLine();


                    try {
                        Transportadora novaTransportadora = new Transportadora(nome, id, cnpj);
                        transportadoras.add(novaTransportadora);
                        System.out.println("Transportadora cadastrada com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:

                    if (transportadoras.isEmpty()) {
                        System.out.println("Nenhuma transportadora cadastrada.");
                    } else {
                        System.out.println("Lista de transportadoras:");
                        for (Transportadora t : transportadoras) {
                            System.out.println("ID: " + t.getId() + ", Nome: " + t.getNome() + ", CNPJ: " + t.getCnpj());
                        }
                    }
                    break;

                case 3:

                    System.out.println("Digite o ID da transportadora que deseja atualizar: ");
                    int idAtualizacao = scanner.nextInt();
                    scanner.nextLine();

                    Transportadora transportadoraEncontrada = null;
                    for (Transportadora t : transportadoras) {
                        if (t.getId() == idAtualizacao) {
                            transportadoraEncontrada = t;
                            break;
                        }
                    }

                    if (transportadoraEncontrada != null) {
                        System.out.println("Digite o novo nome da transportadora: ");
                        String novoNome = scanner.nextLine();
                        transportadoraEncontrada.setNome(novoNome);

                        System.out.println("Digite o novo CNPJ da transportadora: ");
                        String novoCnpj = scanner.nextLine();
                        try {
                            transportadoraEncontrada.setCnpj(novoCnpj);
                            System.out.println("Transportadora atualizada com sucesso!");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Transportadora com ID " + idAtualizacao + " não encontrada.");
                    }
                    break;

                case 4:

                    System.out.println("Total de transportadoras cadastradas: " + transportadoras.size());
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