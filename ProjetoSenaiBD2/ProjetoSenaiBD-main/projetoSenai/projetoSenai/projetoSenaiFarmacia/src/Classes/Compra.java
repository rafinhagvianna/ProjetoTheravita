package Classes;

import Enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;

public class Compra extends Transacoes {

	public Compra() {
		super();
	}

	public Compra(ArrayList<Itens> produtos, LocalDate data, Funcionario funcionario, Status status, double valor) {
		super(produtos, data, funcionario, status, valor);
	}

	public double calculaTotal() {
		double total = 0;

		if (getProdutos() != null) {
			for (Itens item : getProdutos()) {
				total += item.valorCompra();
			}
		} else {
			System.err.println("Aviso: Lista de produtos na compra é nula. Calculando total sem itens.");
		}
		return total;
	}
}