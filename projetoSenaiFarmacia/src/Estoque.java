public class Estoque {
    private Produto produto;
    private int estoque;
    private int minimo;

    public Estoque() {

    }

    public Estoque(int estoque, int minimo, Produto produto) {
        this.estoque = estoque;
        this.minimo = minimo;
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public int verificarMinimo() {
        return 0;
    }
    
    public int verificarVenda(int qtd) {
        return 0;
    }
}
