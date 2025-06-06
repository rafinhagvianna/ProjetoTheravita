import Enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;

public class Compra extends Transacoes {

    public Compra() {
        super();
    }

    public Compra(String id, ArrayList<Produto> produtos, double total, LocalDate data, Funcionario funcionario, ArrayList<Status> status, double valor){
        super(id, produtos, total, data, funcionario, status, valor);
    }

    public void realizarCompra(){
        System.out.println("Compra realizada.");
    }
}
