package Gerenciadores;
import java.util.ArrayList;
import java.util.Scanner;
import Classes.*;
import Interfaces.IntProduto;

public class GerProduto implements IntProduto {

    static Scanner scanner = new Scanner(System.in);

    @Override
    public void menu(){
        System.out.println("Escolha uma das opções: ");
        System.out.println("---------------------------------------------");
        System.out.println("| 1 - Cadastrar produto                     |");
        System.out.println("| 2 - Listar produtos                       |");
        System.out.println("| 3 - Atualizar produtos                    |");
        System.out.println("| 4 - Verificar disponibilidade do produto  |");
        System.out.println("| 0  - Sair                                 |");
        System.out.println("---------------------------------------------");
    }

    @Override
    public void cadastraProduto(ArrayList<Produto> produtos) {
        System.out.print("Descrição = ");
        String descricao = scanner.nextLine();
        System.out.print("Valor de venda = ");
        double valorVenda = scanner.nextDouble();
        System.out.print("Valor de compra = ");
        double valorCompra = scanner.nextDouble();
        scanner.nextLine();

        try {
            Produto produto = new Produto(descricao, valorVenda, valorCompra);
            produtos.add(produto);
            System.out.println("Produto cadastrado com sucesso!");
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void listarProdutos(ArrayList<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto foi cadastrado.");
        } else {
            for (Produto produto : produtos) {
                System.out.println(produto);
            }
        }
    }

    @Override
    public void atualizarProduto(ArrayList<Produto> produtos) {
        System.out.print("Informe o ID do produto a ser atualizado: ");
        String procurarId = scanner.nextLine();
        System.out.print("Informe a descrição do produto a ser atualizado: ");
        String descricaoProduto = scanner.nextLine();


        Produto produtoAtualizar = null;
        for (Produto prod : produtos) {
            if (prod.getDescricao().equalsIgnoreCase(descricaoProduto) && prod.getId().equalsIgnoreCase(procurarId)) {
                produtoAtualizar = prod;
                break;
            }
        }

        if (produtoAtualizar != null) {
            System.out.println("Produto encontrado: " + produtoAtualizar);

            System.out.print("Deseja atualizar o valor de venda? (S/N): ");
            char atualizarValorVenda = scanner.next().charAt(0);
            if (atualizarValorVenda == 'S' || atualizarValorVenda == 's') {
                System.out.print("Novo valor de venda: ");
                double novoValorVenda = scanner.nextDouble();
                produtoAtualizar.setValorVenda(novoValorVenda);
            }

            System.out.print("Deseja atualizar o valor de compra? (S/N): ");
            char atualizarValorCompra = scanner.next().charAt(0);
            if (atualizarValorCompra == 'S' || atualizarValorCompra == 's') {
                System.out.print("Novo valor de compra: ");
                double novoValorCompra = scanner.nextDouble();
                produtoAtualizar.setValorCompra(novoValorCompra);
            }

            System.out.print("Deseja atualizar a descrição do produto? (S/N): ");
            char atualizarDescricao = scanner.next().charAt(0);
            if (atualizarDescricao == 'S' || atualizarDescricao == 's') {
                System.out.print("Nova descrição: ");
                scanner.nextLine();
                String novaDescricao = scanner.nextLine();
                produtoAtualizar.setDescricao(novaDescricao);
            }
            System.out.println("Produto atualizado com sucesso!");
        } else {
            System.out.println("Produto não encontrado!");
            System.out.println("Descrição ou ID digitado incorretamente.");
        }
    }

    public void diponibilidadeProduto(ArrayList<Produto> produtos) {
        System.out.print("Informe a descrição do produto para verificar disponibilidade: ");
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
    }

}
