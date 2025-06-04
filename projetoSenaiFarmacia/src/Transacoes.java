import java.util.ArrayList;
import java.time.LocalDate;

public abstract class Transacoes {
    private int id;
    private ArrayList<Produto> produtos;
    private double total;
    private LocalDate data;
    private Funcionario funcionario;

    public Transacoes() {

    }

    public Transacoes(int id, ArrayList<Produto> produtos, double total, LocalDate data, Funcionario funcionario) {
        this.id = id;
        this.produtos = produtos;
        this.total = total;
        this.data = data;
        this.funcionario = funcionario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
