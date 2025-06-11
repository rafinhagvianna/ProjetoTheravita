package Classes;

import java.time.LocalDate;
import java.util.ArrayList;

public class Caixa {
    private double saldo;
    private ArrayList<Venda> entrada;
    private ArrayList<Compra> saida;

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
                if (!v.getStatus().equals(Enums.Status.CANCELADO))
                    totalEntradas += v.getValor();
            }
        }

        for (Compra c : saida) {
            if (c.getData().getMonthValue() == mes && c.getData().getYear() == ano) {
                if (!c.getStatus().equals(Enums.Status.CANCELADO))
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
                if (!v.getStatus().equals(Enums.Status.CANCELADO))
                    totalEntradas += v.getValor();
            }
        }

        for (Compra c : saida) {
            if (c.getData().getYear() == ano) {
                if (!c.getStatus().equals(Enums.Status.CANCELADO))
                    totalSaidas += c.getValor();
            }
        }

        return totalEntradas - totalSaidas;
    }

    public ArrayList<Venda> filtrarVendaPelaData(LocalDate date){
        ArrayList<Venda> vendasFiltrada = new ArrayList<>();

        for (Venda venda : this.entrada){
            if (venda.getData().equals(date)) {
                vendasFiltrada.add(venda);
            }
        }
        return vendasFiltrada;
    }

    public ArrayList<Compra> filtrarCompraPelaData(LocalDate date){
        ArrayList<Compra> comprasFiltrada = new ArrayList<>();

        for (Compra compra : this.saida){
            if (compra.getData().equals(date)) {
                comprasFiltrada.add(compra);
            }
        }
        return comprasFiltrada;
    } 
}