package Classes;
import java.util.ArrayList;
import java.util.List;

import Exceptions.FuncionarioException;
import Exceptions.ProdutoException;
import Validators.FuncionarioValidator;
import Validators.ProdutoValidator;

public class Produto {
    private static int idBase = 1;
    private String descricao;
    private String id;
    private double valorVenda;
    private double valorCompra;
    private Estoque estoqueProduto;
    private static List<Produto> catalogo = new ArrayList<>();

    public Produto() {

    }

    public Produto(String descricao, double valorVenda, double valorCompra) throws ProdutoException {
        this.descricao = descricao;
        this.id = "PROD" + idBase;
        this.valorVenda = valorVenda;
        this.valorCompra = valorCompra;
        idBase++;
        this.estoqueProduto = new Estoque(0, 5, this);
        catalogo.add(this);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws ProdutoException {
        if (ProdutoValidator.isValidDescricao(descricao)) {
            this.descricao = descricao;
        } else {
            throw new ProdutoException();
        }
    }

    public String getId() {
        return id;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) throws ProdutoException{
        if (ProdutoValidator.isValidValor(valorVenda) || valorVenda > this.valorCompra) {
            this.valorVenda = valorVenda;
        } else {
            throw new ProdutoException();
        }
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) throws ProdutoException{
        if (ProdutoValidator.isValidValor(valorCompra)) {
            this.valorCompra = valorCompra;
        } else {
            throw new ProdutoException();
        }
        
    }

    public Estoque getEstoqueProduto() {
        return estoqueProduto;
    }
    public void setEstoqueProduto(Estoque estoqueProduto) {
        this.estoqueProduto = estoqueProduto;
    }

    @Override
    public String toString() {
        return  "Nome do produto = " + descricao +
                ", id = " + id +
                ", Valor de venda = R$" + valorVenda +
                ", Valor de compra = R$" + valorCompra +
                ", Estoque = " + (estoqueProduto != null ? estoqueProduto.getEstoque() : "N/A");
    }

    public static void getCatalogo() {
        for (Produto produto : catalogo) {
            System.out.println(produto);
        }
    }

    public boolean verificaEstoque(int qtd){
        return qtd <= this.estoqueProduto.getEstoque();
    }
}