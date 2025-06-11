package Gerenciadores;

import java.util.ArrayList;
import java.util.Scanner;
import Classes.*;
import Exceptions.ProdutoException;
import Interfaces.IntProduto;

public class GerProduto implements IntProduto {

    static Scanner scanner = new Scanner(System.in);

    @Override
    public void menu() {
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
        String descricao = "";
        double valorVenda = 0;
        double valorCompra = 0;

        System.out.print("Descrição = ");
        descricao = scanner.nextLine();
        do {
            try {
                System.out.print("Valor de venda = ");
                valorVenda = scanner.nextDouble();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Tipo inserido inválido. Digite um valor real!");
                valorVenda = 0;
                
            }

        } while (valorVenda <= 0);

        do {
            try {
                System.out.print("Valor de compra = ");
                valorCompra = scanner.nextDouble();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Tipo inserido inválido. Digite um valor real!");
                valorCompra = 0;
            }

        } while (valorCompra <= 0);

        Produto produto;
        try {
            produto = new Produto(descricao, valorVenda, valorCompra);
        } catch (ProdutoException e) {
            produto = e.CadastroException(descricao, valorVenda, valorCompra);
        }
        produtos.add(produto);
        System.out.println("Produto cadastrado com sucesso!");

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

        Produto produtoAtualizar = null;
        String procurarId;
        do {
            System.out.print("Informe o ID do produto a ser atualizado: ");
            procurarId = scanner.nextLine();
            produtoAtualizar = Produto.buscarProdutoPorId(procurarId, produtos);


                if (produtoAtualizar != null) {
                    System.out.println("Produto encontrado: " + produtoAtualizar);

                    System.out.print("Deseja atualizar o valor de venda? (S/N): ");
                    char atualizarValorVenda = scanner.next().charAt(0);
                    if (atualizarValorVenda == 'S' || atualizarValorVenda == 's') {
                        double novoValorVenda = 0;

                        do {
                            try {
                                System.out.print("Novo valor de venda: ");
                                novoValorVenda = scanner.nextDouble();
                                scanner.nextLine(); 
                            } catch (Exception e) {
                                scanner.nextLine();
                                System.out.println("Tipo inserido inválido. Digite um valor real!");
                                novoValorVenda = 0;
                            }

                        } while (novoValorVenda <= 0);

                        try {
                            produtoAtualizar.setValorVenda(novoValorVenda);
                        } catch (ProdutoException e) {
                            e.ValorVendaException(produtoAtualizar);
                        }
                    }

                    System.out.print("Deseja atualizar o valor de compra? (S/N): ");
                    char atualizarValorCompra = scanner.next().charAt(0);
                    if (atualizarValorCompra == 'S' || atualizarValorCompra == 's') {
                        double novoValorCompra = 0;

                        do {
                            try {
                                System.out.print("Novo valor de compra: ");
                                novoValorCompra = scanner.nextDouble();
                                scanner.nextLine();
                            } catch (Exception e) {
                                scanner.nextLine();
                                System.out.println("Tipo inserido inválido. Digite um valor real!");
                                novoValorCompra = 0;
                            }

                        } while (novoValorCompra <= 0);


                        try {
                            produtoAtualizar.setValorCompra(novoValorCompra);
                        } catch (ProdutoException e) {
                            e.ValorCompraException(produtoAtualizar);
                        }
                    }

                    System.out.print("Deseja atualizar a descrição do produto? (S/N): ");
                    char atualizarDescricao = scanner.next().charAt(0);
                    if (atualizarDescricao == 'S' || atualizarDescricao == 's') {
                        System.out.print("Nova descrição: ");
                        String novaDescricao = scanner.nextLine();
                        try {
                            produtoAtualizar.setDescricao(novaDescricao);
                        } catch (ProdutoException e) {
                            e.DescricaoException(produtoAtualizar);
                        }
                    }
                    System.out.println("Produto atualizado com sucesso!");
                }

             else {
                System.out.println("Produto não encontrado");
            }
        } while (produtoAtualizar == null);

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
