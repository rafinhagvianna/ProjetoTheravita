package Gerenciadores;

import Classes.Compra;
import Classes.Venda;
import Enums.Status;

import java.util.ArrayList;
import java.util.Scanner;

public class GerGestao {
    private ArrayList<Compra> compras;
    private ArrayList<Venda> vendas;

    public GerGestao(ArrayList<Compra> compras, ArrayList<Venda> vendas) {
        this.compras = compras;
        this.vendas = vendas;
    }

    public void apresentarMenuGestao(Scanner scanner) {
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
                    atualizarStatus(scanner);
                    break;
                case 0:
                    System.out.println("Retornando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcaoUsuarioGestao != 0);
    }

    private void consultarNegocios() {
        boolean temNegocios = false;

        System.out.println("\n--- Compras em andamento ---");
        for (int i = 0; i <= compras.size(); i++) {
            if (compras.get(i).getStatus() == Status.ABERTO) {
                System.out.println("Compra #" + i + " - " + compras.get(i));
                temNegocios = true;
            }
        }

        System.out.println("\n--- Vendas em andamento ---");
        for (int i = 0; i <= vendas.size(); i++) {
            if (vendas.get(i).getStatus() == Status.ABERTO) {
                System.out.println("Venda #" + i + " - " + vendas.get(i));
                temNegocios = true;
            }
        }

        if (!temNegocios) {
            System.out.println("Não há compras ou vendas em andamento.");
        }
    }

    private void atualizarStatus(Scanner scanner) {
        System.out.print("Digite o índice da compra a ser finalizada: ");
        int indiceCompra = scanner.nextInt();
        if (indiceCompra >= 0 && indiceCompra <= compras.size()) {
            compras.get(indiceCompra).setStatus(Status.FECHADO);
            System.out.println("Compra #" + indiceCompra + " finalizada.");
        } else {
            System.out.println("Índice de compra inválido.");
        }

        System.out.print("Digite o índice da venda a ser finalizada: ");
        int indiceVenda = scanner.nextInt();
        if (indiceVenda >= 0 && indiceVenda <= vendas.size()) {
            vendas.get(indiceVenda).setStatus(Status.FECHADO);
            System.out.println("Venda #" + indiceVenda + " finalizada.");
        } else {
            System.out.println("Índice de venda inválido.");
        }
    }
}
