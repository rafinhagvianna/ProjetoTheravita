package Gerenciadores;

import Classes.Caixa;
import Classes.Compra;
import Classes.Itens;
import Classes.Venda;
import Enums.Status;
import Interfaces.IntGestao;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class GerGestao implements IntGestao {
    private ArrayList<Compra> compras;
    private ArrayList<Venda> vendas;

    public GerGestao(ArrayList<Compra> compras, ArrayList<Venda> vendas) {
        this.compras = compras;
        this.vendas = vendas;
    }

    public void apresentarMenuGestao(Scanner scanner, Caixa caixa) {
        int opcaoUsuarioGestao;
        do {
            System.out.println("\nEscolha uma das opções: ");
            System.out.println("--------------------------------------------");
            System.out.println("| 1 - Consultar negócios em andamento      |");
            System.out.println("| 2 - Atualizar status                     |");
            System.out.println("| 0 - Sair                                 |");
            System.out.println("--------------------------------------------");
            System.out.print("Opção: ");
            opcaoUsuarioGestao = scanner.nextInt();

            switch (opcaoUsuarioGestao) {
                case 1:
                    consultarNegocios();
                    break;
                case 2:
                    atualizarStatus(scanner, caixa);
                    break;
                case 0:
                    System.out.println("Retornando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcaoUsuarioGestao != 0);
    }

    public void consultarNegocios() {
        boolean statusCompra = false, statusVenda = false;

        System.out.println("\n--- Compras em andamento ---");
        for (Compra compra : compras) {
            if (compra.getStatus() == Status.ABERTO) {
                System.out.println("Compra #" + compra.getId() + " - " + compra);
                statusCompra = true;
            }
        }
        if (!statusCompra) {
            System.out.println("Não há compras em andamentos.");
        }

        System.out.println("\n--- Vendas em andamento ---");
        for (Venda venda : vendas) {
            if (venda.getStatus() == Status.ABERTO) {
                System.out.println("Venda #" + venda.getId() + " - " + venda);
                statusVenda = true;
            }
        }
        if (!statusVenda) {
            System.out.println("Não há vendas em andamentos.");
        }
    }

    public void atualizarStatus(Scanner scanner, Caixa caixa) {
        System.out.println("Insira o tipo de transação que deseja atualizar: ");
        System.out.println("1 - Compra");
        System.out.println("2 - Venda");
        int opc = scanner.nextInt();
        scanner.nextLine();

        LocalDate data = null;

        do {
            try {
                System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
                String dtPesquisa = scanner.nextLine();

                if (dtPesquisa.equalsIgnoreCase("HJ")) {
                    data = LocalDate.now();
                } else {
                    data = LocalDate.parse(dtPesquisa);
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data não aceito!");
                data = null;
            }
        } while (data == null);

        if (opc == 1) {
            ArrayList<Compra> comprasPorData = caixa.filtrarCompraPelaData(data);
            comprasPorData.forEach(System.out::println);

            System.out.println("Insira o ID da compra que deseja atualizar: ");
            String id = scanner.nextLine();

            System.out.println("Escolha para qual status deseja atualizar: ");
            System.out.println("1 - CONCLUIR");
            System.out.println("2 - CANCELAR");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            Compra compraSelecionada = comprasPorData.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (compraSelecionada != null) {
                if (opcao == 1) {
                    compraSelecionada.setStatus(Status.FECHADO);
                } else if (opcao == 2) {
                    compraSelecionada.setStatus(Status.CANCELADO);
                    for (Itens item : compraSelecionada.getProdutos()) {
                        item.getProduto().getEstoqueProduto().realizaTransicao(-item.getQuantidade());
                    }
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                System.out.println("ID não encontrado.");
            }
        } else if (opc == 2) {
            ArrayList<Venda> vendasPorData = caixa.filtrarVendaPelaData(data);
            vendasPorData.forEach(System.out::println);

            System.out.println("Insira o ID da venda que deseja atualizar: ");
            String id = scanner.nextLine();

            System.out.println("Escolha para qual status deseja atualizar: ");
            System.out.println("1 - CONCLUIR");
            System.out.println("2 - CANCELAR");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            Venda vendaSelecionada = vendasPorData.stream()
                    .filter(v -> v.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (vendaSelecionada != null) {
                if (opcao == 1) {
                    vendaSelecionada.setStatus(Status.FECHADO);
                } else if (opcao == 2) {
                    vendaSelecionada.setStatus(Status.CANCELADO);
                    for (Itens item : vendaSelecionada.getProdutos()) {
                        item.getProduto().getEstoqueProduto().realizaTransicao(item.getQuantidade());
                    }
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                System.out.println("ID não encontrado.");
            }
        } else {
            System.out.println("Opção inválida. Tente novamente.");
        }
    }
}