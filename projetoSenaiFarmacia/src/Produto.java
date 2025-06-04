public class Produto {
    private String descricao;
    private int id;
    private double valorVenda;
    private double valorCompra;

    public Produto() {

    }

    public Produto(String descricao, int id, double valorVenda, double valorCompra) {
        this.descricao = descricao;
        this.id = id;
        this.valorVenda = valorVenda;
        this.valorCompra = valorCompra;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
