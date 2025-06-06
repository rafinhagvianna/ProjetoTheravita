import Enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venda extends Transacoes {
    private Transportadora transportadora;

    public Venda() {
        super();
    }

    public Venda(double valor, LocalDate data) {
        super("TX" + proxId++, new ArrayList<Produto>(), valor, data, null, new ArrayList<Status>(), valor);
    }

    public Venda(String id, ArrayList<Itens> itens, double total, LocalDate data, Funcionario funcionario, ArrayList<Status> status, double valor) {
        super(id, itens, total, data, funcionario, status, valor);
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

    public double calculaTotal() {
        return 0;
    }

    public static void emAberto(ArrayList<Venda> vendas) {
        
    }
}
