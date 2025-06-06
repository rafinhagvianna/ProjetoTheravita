import java.util.ArrayList;
import java.util.List;

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

    public boolean verificarEstoque() { // Para o método verificarVenda na classe Venda
        if (this.estoque < this.minimo) {
            System.out.println("ATENÇÃO: O estoque de " + this.produto.getDescricao() + " está em " + this.estoque + " unidades, o que é abaixo do mínimo de " + this.minimo + ". Necessário nova compra.");
            return false;
        } else {
            System.out.println("O estoque de " + this.produto.getDescricao() + " está OK (" + this.estoque + " unidades). Não é necessário nova compra.");
            return true;
        }
    }
}
