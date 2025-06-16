package Gerenciadores;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Classes.*;
import Enums.Regiao;
import Interfaces.IntCaixa;

public class GerCaixa implements IntCaixa {
    Scanner scanner = new Scanner(System.in);

    public void menu() {
        System.out.println("Escolha uma das opções: ");
        System.out.println("--------------------------------------------");
        System.out.println("| 1 - Registrar entrada                    |");
        System.out.println("| 2 - Registrar saída                      |");
        System.out.println("| 3 - Visualizar saldo atual               |");
        System.out.println("| 4 - Verificar lucro mensal               |");
        System.out.println("| 5 - Verificar lucro anual                |");
        System.out.println("| 6 - Gerar relatório                      |");
        System.out.println("| 0 - Sair                                 |");
        System.out.println("--------------------------------------------");
        System.out.println();
    }

    public void registrarEntrada(Caixa caixa, Venda novaVenda, ArrayList<Funcionario> funcionarios,
            ArrayList<Transportadora> transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos) {
        if (funcionarios.size() < 1 || produtos.size() < 1 || transportadoras.size() < 1) {
            System.out.println("Não há funcionários, produtos ou transportadoras suficientes para essa ação!");
        } else {
            novaVenda = realizarVenda(scanner, funcionarios, transportadoras, setores, produtos);
            if (novaVenda != null) {
                caixa.getEntrada().add(novaVenda);
                if (novaVenda.getStatus().equals(Enums.Status.FECHADO))
                    caixa.setSaldo(caixa.getSaldo() + novaVenda.getValor());
                System.out.println("Venda registrada com sucesso!");
            }
        }
    }

    public void registrarSaida(Caixa caixa, Compra novaCompra, ArrayList<Funcionario> funcionarios,
            ArrayList<Produto> produtos) {
        if (funcionarios.size() < 1 || produtos.size() < 1) {
            System.out.println("Não há funcionários ou produtos suficientes para essa ação!");
        } else {
            novaCompra = realizarCompra(scanner, funcionarios, produtos);
            if (novaCompra != null) {
                caixa.getSaida().add(novaCompra);
                if (novaCompra.getStatus().equals(Enums.Status.FECHADO))
                    caixa.setSaldo(caixa.getSaldo() - novaCompra.getValor());
                System.out.println("Compra registrada com sucesso!");
            }
        }
    }

    public static Venda realizarVenda(Scanner scanner, ArrayList<Funcionario> funcionarios,
            ArrayList<Transportadora> transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos) {

        int quantidade;
        String prod;

        Venda venda = new Venda();
        Funcionario funcionarioVenda = null;
        Regiao regiao;
        Transportadora transportadora = null;
        ArrayList<Itens> itens = new ArrayList<>();

        System.out.println("Iniciando nova venda...");

        do {
            System.out.println("Insira o id do produto ou 0 para parar: ");
            prod = scanner.nextLine();

            if (!prod.equals("0")) {
                Produto produto = Produto.buscarProdutoPorId(prod, produtos);

                if (produto != null) {
                    try {
                        System.out.println("Quantas unidades do produto " + produto.getDescricao()
                                + " deseja adicionar? (0 para cancelar)");
                        quantidade = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Tipo inserido não aceito, digite um número inteiro!");
                        quantidade = 0;
                    }

                    if (quantidade != 0) {
                        if (produto.verificaEstoque(quantidade)) {
                            produto.getEstoqueProduto().realizaTransicao(-quantidade);
                            Itens item = new Itens(quantidade, produto);
                            itens.add(item);
                        } else {
                            System.out.println("Quantidade excede a disponivel em estoque!");
                        }

                    }
                } else {
                    System.out.println("Produto não encontrado");
                }

            }
        } while (!prod.equals("0"));

        if (itens.size() > 0) {
            venda.setProdutos(itens);

            do {
                System.out.print("Informe o ID do funcionário: ");
                String idFuncionario = scanner.nextLine();
                funcionarioVenda = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

                if (funcionarioVenda != null) {
                    venda.setFuncionario(funcionarioVenda);
                } else {
                    System.out.println("\nFuncionário não encontrado !\n");
                }
            } while (funcionarioVenda == null);

            LocalDate dtVenda = null;
            do {
                try {
                    System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
                    String dataVenda = scanner.nextLine();

                    if (dataVenda.equalsIgnoreCase("HJ")) {
                        dtVenda = LocalDate.now();
                    } else {
                        dtVenda = LocalDate.parse(dataVenda);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data não aceito!");
                    dtVenda = null;
                }
            } while (dtVenda == null);
            venda.setData(dtVenda);

            if (dtVenda.isAfter(LocalDate.now())) {
                venda.setStatus(Enums.Status.ABERTO);
            } else {
                venda.setStatus(Enums.Status.FECHADO);
            }

            do {
                try {
                    System.out.println("Escolha a região de entrega da venda:");
                    Regiao[] regioes = Regiao.values();
                    for (int i = 0; i < regioes.length; i++) {
                        System.out.println((i + 1) + " - " + regioes[i]);
                    }
                    int regiaoEscolhida = scanner.nextInt();
                    scanner.nextLine();
                    regiao = regioes[regiaoEscolhida - 1];
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Opção inválida! Tente novamente.");
                    regiao = null;
                }
            } while (regiao == null);

            do {

                try {
                    System.out.println("Escolha a transportadora para a venda:");
                    for (int i = 0; i < transportadoras.size(); i++) {
                        System.out.println((i + 1) + " - " + transportadoras.get(i).getNome());
                    }
                    int transportadoraEscolhida = scanner.nextInt() - 1;
                    scanner.nextLine();
                    transportadora = transportadoras.get(transportadoraEscolhida);
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Opção inválida! Tente novamente.");
                    transportadora = null;
                }

                if (!transportadora.atendeRegiao(regiao) && transportadora != null) {
                    System.out.println("Transportadora não atende região definida!");
                    transportadora = null;
                }
            } while (transportadora == null);

            venda.setTransportadora(transportadora);

            venda.setValor(venda.calculaTotal());

            return venda;
        } else {
            System.out.println("Nenhum produto foi definido!");

            return null;
        }

    }

    public static Compra realizarCompra(Scanner scanner, ArrayList<Funcionario> funcionarios,
            ArrayList<Produto> produtos) {
        int quantidade;
        String prod;
        Compra compra = new Compra();
        Funcionario funcionarioCompra = null;
        ArrayList<Itens> itens = new ArrayList<>();

        System.out.println("Iniciando nova compra...");

        int contador = 0;
        do {
            if (contador == 0) {
                System.out.println("Insira o id do produto ou 0 para cancelar a compra: ");
            } else {
                System.out.println("Insira o id do novo produto ou 0 para finalizar a compra: ");
            }
            prod = scanner.nextLine();

            if (!prod.equals("0")) {
                Produto produto = Produto.buscarProdutoPorId(prod, produtos);

                if (produto != null) {

                    try {
                        System.out.println("Quantas unidades do produto " + produto.getDescricao()
                                + " deseja adicionar? (0 para cancelar)");
                        quantidade = scanner.nextInt();
                        scanner.nextLine();
                    } catch (Exception e) {
                        scanner.nextLine();
                        System.out.println("Tipo inserido não aceito, digite um número inteiro!");
                        quantidade = 0;
                    }

                    if (quantidade != 0) {
                        Itens item = new Itens(quantidade, produto);
                        itens.add(item);
                        produto.getEstoqueProduto().realizaTransicao(quantidade);
                    }
                } else {
                    System.out.println("Produto não encontrado");
                }
            }
            contador = 1;
        } while (!prod.equals("0"));

        if (itens.size() > 0) {
            compra.setProdutos(itens);

            do {
                System.out.print("Informe o ID do funcionário: ");
                String idFuncionario = scanner.nextLine();
                funcionarioCompra = Funcionario.buscarFuncionarioPorId(idFuncionario, funcionarios);

                if (funcionarioCompra != null) {
                    compra.setFuncionario(funcionarioCompra);
                } else {
                    System.out.println("\nFuncionário não encontrado !\n");
                }
            } while (funcionarioCompra == null);

            LocalDate dtCompra;
            do {

                try {
                    System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
                    String dataCompra = scanner.nextLine();
                    if (dataCompra.equalsIgnoreCase("HJ")) {
                        dtCompra = LocalDate.now();
                    } else {
                        dtCompra = LocalDate.parse(dataCompra);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data não aceito!");
                    dtCompra = null;
                }

            } while (dtCompra == null);

            compra.setData(dtCompra);

            if (dtCompra.isAfter(LocalDate.now())) {
                compra.setStatus(Enums.Status.ABERTO);
            } else {
                compra.setStatus(Enums.Status.FECHADO);
                ;
            }

            compra.setValor(compra.calculaTotal());
            return compra;
        } else {
            System.out.println("Compra cancelada!");
            return null;
        }

    }
}
