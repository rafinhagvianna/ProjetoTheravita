import java.util.ArrayList;

public class Caixa {
    private double saldo;
    private ArrayList<Venda> entrada;
    private ArrayList<Compra> saida;

    public Caixa(double saldo, ArrayList<Venda> entrada, ArrayList<Compra> saida) {
        this.saldo = saldo;
        this.entrada = entrada;
        this.saida = saida;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public ArrayList<Venda> getEntrada() {
        return entrada;
    }

    public void setEntrada(ArrayList<Venda> entrada) {
        this.entrada = entrada;
    }

    public ArrayList<Compra> getSaida() {
        return saida;
    }

    public void setSaida(ArrayList<Compra> saida) {
        this.saida = saida;
    }
}
