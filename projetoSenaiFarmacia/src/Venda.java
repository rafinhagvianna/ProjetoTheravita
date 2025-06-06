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

    // Adiciona um item à venda
    public void adicionarItem(Itens item) {
        getProdutos().add(item);
    }

    // Remove um item da venda
    public void removerItem(Itens item) {
        getProdutos().remove(item);
    }

    // Calcula o total da venda
    public double calculaTotal() {
        double total = 0;
        for (Itens item : getProdutos()) {
            total += item.valorVenda();
        }
        total += transportadora.getTaxa() * total;
        return total;
    }

    public static void emAberto(ArrayList<Venda> vendas) {
        for (Venda venda : vendas) {
            if (venda.getStatus().equals(Status.ABERTO)) {
                System.out.println("Venda em aberto: " + venda.getId());
            }
        }
    }
}
