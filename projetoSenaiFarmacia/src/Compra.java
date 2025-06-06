import Enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;

public class Compra extends Transacoes {

    public Compra() {
        super();
    }

    public Compra( ArrayList<Itens> produtos, LocalDate data, Funcionario funcionario, Status status, double valor){
        super(produtos, data, funcionario, status, valor);
    }

    public void realizarCompra(){
        System.out.println("Compra realizada.");
    }
}