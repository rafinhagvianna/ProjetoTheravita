import java.util.ArrayList;
import java.time.LocalDate;

public class Caixa {
    private double saldo;
    private ArrayList<Venda> entrada;
    private ArrayList<Compra> saida;
    private LocalDate hoje = LocalDate.now();

    public Caixa() {
        entrada = new ArrayList<>();
        saida = new ArrayList<>();
    }

  
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
    public double totalCaixa() {
        double totalEntradas = 0;
        double totalSaidas = 0;

        for (Venda v : entrada) {
            totalEntradas += v.getValor();
        }

        for (Compra c : saida) {
            totalSaidas += c.getValor();
        }

        saldo = totalEntradas - totalSaidas;
        return saldo;
    }


    public double lucroMensal(int mes, int ano) {
        double totalEntradas = 0;
        double totalSaidas = 0;

        for (Venda v : entrada) {
            if (v.getData().getMonthValue() == mes && v.getData().getYear() == ano) {
                totalEntradas += v.getValor();
            }
        }

        for (Compra c : saida) {
            if (c.getData().getMonthValue() == mes && c.getData().getYear() == ano) {
                totalSaidas += c.getValor();
            }
        }

        return totalEntradas - totalSaidas;
    }


    public double lucroAnual(int ano) {
        double totalEntradas = 0;
        double totalSaidas = 0;

        for (Venda v : entrada) {
            if (v.getData().getYear() == ano) {
                totalEntradas += v.getValor();
            }
        }

        for (Compra c : saida) {
            if (c.getData().getYear() == ano) {
                totalSaidas += c.getValor();
            }
        }

        return totalEntradas - totalSaidas;
    }
}
