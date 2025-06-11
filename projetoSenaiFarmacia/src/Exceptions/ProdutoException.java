package Exceptions;

import java.util.Scanner;

import Classes.Produto;
import Validators.ProdutoValidator;

public class ProdutoException extends Exception {

    Scanner scanner = new Scanner(System.in);

    public Produto CadastroException(String descricao, double valorVenda, double valorCompra) {

        while (!ProdutoValidator.isValidDescricao(descricao)) {
            System.out.println("Insira a descricao do produto:");
            descricao = scanner.next();
        }
        while (!ProdutoValidator.isValidValor(valorCompra)) {
            System.out.println("Insira o valor da compra:");
            valorCompra = scanner.nextDouble();
        }
        while (!ProdutoValidator.isValidValor(valorVenda) || valorVenda <= valorCompra) {
            if (valorVenda <= valorCompra) System.out.println("O valor da venda deve ser maior que o da compra !");
            System.out.println("Insira o valor da venda:");
            valorVenda = scanner.nextDouble();
        }

        try {
            return new Produto(descricao, valorVenda, valorCompra);
        } catch (ProdutoException e) {
            System.out.println("teste");
            return null;
        }
    }

    public void DescricaoException(Produto prod) {
        try {
            prod.setDescricao(scanner.next());
        }catch(ProdutoException nte){
            nte.DescricaoException(prod);
        }
    }

    public void ValorVendaException(Produto prod) {
        try {
            prod.setValorVenda(scanner.nextDouble());
        }catch(ProdutoException nte){
            nte.ValorVendaException(prod);
        }
    }
    public void ValorCompraException(Produto prod) {
        try {
            prod.setValorCompra(scanner.nextDouble());
        }catch(ProdutoException nte){
            nte.ValorCompraException(prod);
        }
    }

    public void EstoqueException(Produto prod) {
        try {
            prod.getEstoqueProduto().setEstoque(scanner.nextInt());
        }catch(ProdutoException nte){
            nte.EstoqueException(prod);
        }
    }
    public void MinimoException(Produto prod) {
        try {
            prod.getEstoqueProduto().setMinimo(scanner.nextInt());
        }catch(ProdutoException nte){
            nte.MinimoException(prod);
        }
    }

}
