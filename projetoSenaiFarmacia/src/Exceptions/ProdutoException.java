package Exceptions;

import java.util.Scanner;

import Classes.Produto;
import Validators.ProdutoValidator;

public class ProdutoException extends Exception {

    Scanner scanner = new Scanner(System.in);

    public Produto CadastroException(String descricao, double valorVenda, double valorCompra) {

        do {
            System.out.println("Insira a descricao do produto:");
            descricao = scanner.next();
        }while (!ProdutoValidator.isValidDescricao(descricao));
        
        do{
            System.out.println("Insira o valor da compra:");
            valorCompra = scanner.nextDouble();
        }while (!ProdutoValidator.isValidValor(valorCompra)) ;

        do {
            if (valorVenda <= valorCompra) System.out.println("O valor da venda deve ser maior que o da compra !");
            System.out.println("Insira o valor da venda:");
            valorVenda = scanner.nextDouble();
        }while (!ProdutoValidator.isValidValor(valorVenda) || valorVenda <= valorCompra);

        try {
            return new Produto(descricao, valorVenda, valorCompra);
        } catch (ProdutoException e) {
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
            scanner.nextLine();
        }catch(ProdutoException nte){
            scanner.nextLine();
            nte.ValorVendaException(prod);
        }
        catch(Exception e){
            scanner.nextLine();
            System.out.println("Tipo inserido inválido! Digite um número real!");
            ValorVendaException(prod);
        }
    }
    public void ValorCompraException(Produto prod) {
        try {
            prod.setValorCompra(scanner.nextDouble());
            scanner.nextLine();
        }catch(ProdutoException nte){
            scanner.nextLine();
            nte.ValorCompraException(prod);
        }
        catch(Exception e){
            scanner.nextLine();
            System.out.println("Tipo inserido inválido! Digite um número real!");
            ValorCompraException(prod);
        }
    }

    public void EstoqueException(Produto prod) {
        try {
            prod.getEstoqueProduto().setEstoque(scanner.nextInt());
            scanner.nextLine();
        }catch(ProdutoException nte){
            scanner.nextLine();
            nte.EstoqueException(prod);
        }
        catch(Exception e){
            System.out.println("Tipo inserido inválido! Digite um número real!");
            scanner.nextLine();
            EstoqueException(prod);
        }
    }
    public void MinimoException(Produto prod) {
        try {
            prod.getEstoqueProduto().setMinimo(scanner.nextInt());
            scanner.nextLine();
        }catch(ProdutoException nte){
            scanner.nextLine();
            nte.MinimoException(prod);
        }
        catch(Exception e){
            scanner.nextLine();
            System.out.println("Tipo inserido inválido! Digite um número real!");
            MinimoException(prod);
        }
    }

}
