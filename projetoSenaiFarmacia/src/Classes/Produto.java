package Classes;
import java.util.ArrayList;
import java.util.List;

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

    public Produto(String descricao, double valorVenda, double valorCompra) {
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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
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
}