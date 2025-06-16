package Classes;
import Enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venda extends Transacoes {
    private Transportadora transportadora;

    public Venda() {
        super();
    }

    public Venda(double valor, LocalDate data) {
        super(new ArrayList<Itens>(), data, null, null, valor);
    }

    public Venda(ArrayList<Itens> itens, LocalDate data, Funcionario funcionario, Status status, double valor) {
        super( itens, data, funcionario, status, valor);
    }

    public Venda(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public void adicionarItem(Itens item) {
        getProdutos().add(item);
    }

    public void removerItem(Itens item) {
        getProdutos().remove(item);
    }

    public double calculaTotal() {
        double total = 0;
        for (Itens item : getProdutos()) {
            total += item.valorVenda();
        }
        if (transportadora == null) {
            throw new IllegalStateException("Venda não pode ser finalizada sem transportadora.");
        }
        total += (transportadora.getTaxa()/100) * total;
        return total;
    }
}
