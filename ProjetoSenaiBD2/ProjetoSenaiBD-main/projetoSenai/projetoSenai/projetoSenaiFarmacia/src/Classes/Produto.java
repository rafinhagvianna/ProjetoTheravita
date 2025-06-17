package Classes;

import Exceptions.ProdutoException;
import Validators.ProdutoValidator;

public class Produto {
	private String idBanco;
	private String descricao;
	private double valorVenda;
	private double valorCompra;
	private Estoque estoqueProduto;

//	public Produto() {
//	}

	public Produto(String idBanco, String descricao, double valorVenda, double valorCompra) throws ProdutoException {
		if (!ProdutoValidator.isValidDescricao(descricao)) {
			throw new ProdutoException("A descrição do produto é inválida.");
		}
		if (!ProdutoValidator.isValidValor(valorVenda)) {
			throw new ProdutoException("O valor de venda do produto é inválido.");
		}
		if (!ProdutoValidator.isValidValor(valorCompra)) {
			throw new ProdutoException("O valor de compra do produto é inválido.");
		}
		if (valorVenda <= valorCompra) {
			throw new ProdutoException("O valor de venda deve ser maior que o valor de compra.");
		}

		this.idBanco = idBanco;
		this.descricao = descricao;
		this.valorVenda = valorVenda;
		this.valorCompra = valorCompra;
		this.estoqueProduto = new Estoque(0, 5, this);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws ProdutoException {
		if (ProdutoValidator.isValidDescricao(descricao)) {
			this.descricao = descricao;
		} else {
			throw new ProdutoException("Descrição do produto não pode ser vazia.");
		}
	}

	public String getId() {
		return idBanco;
	}

	public double getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(double valorVenda) throws ProdutoException {

		if (ProdutoValidator.isValidValor(valorVenda)) {

			this.valorVenda = valorVenda;
		} else {
			throw new ProdutoException("Valor de venda inválido.");
		}
	}

	public double getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(double valorCompra) throws ProdutoException {
		if (ProdutoValidator.isValidValor(valorCompra)) {
			this.valorCompra = valorCompra;
		} else {
			throw new ProdutoException("Valor de compra inválido.");
		}
	}

	public Estoque getEstoqueProduto() {
		return estoqueProduto;
	}

	public void setEstoqueProduto(Estoque estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}

	@Override
	public String toString() {
		return "\nNome = " + descricao + ", \nID = " + idBanco + ", \nValor de venda = R$" + valorVenda
				+ ", \nValor de compra = R$" + valorCompra + ", \nEstoque = "
				+ (estoqueProduto != null ? estoqueProduto.getEstoque() : "N/A");
	}

	public boolean verificaEstoque(int qtd) {
		return estoqueProduto != null && qtd <= this.estoqueProduto.getEstoque();
	}
}