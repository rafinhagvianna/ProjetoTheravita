package Gerenciadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import Classes.ConexaoBD;
import Classes.Produto;
import Classes.Estoque;
import Exceptions.ProdutoException;
import Interfaces.IntProduto;

public class GerProduto implements IntProduto {
	static Scanner scanner = new Scanner(System.in);

	@Override
	public void menu() {
		System.out.println("Escolha uma das opções: ");
		System.out.println("---------------------------------------------");
		System.out.println("| 1 - Cadastrar produto                     |");
		System.out.println("| 2 - Listar produtos                       |");
		System.out.println("| 3 - Atualizar produtos                    |");
		System.out.println("| 4 - Verificar disponibilidade do produto  |");
		System.out.println("| 0  - Sair                                 |");
		System.out.println("---------------------------------------------");
	}

	@Override
	public void cadastraProduto() {
		System.out.print("Descrição = ");
		String nome = scanner.nextLine().trim();

		double valorVenda = 0;
		do {
			try {
				System.out.print("Valor de venda = ");
				valorVenda = scanner.nextDouble();
				scanner.nextLine();
				if (valorVenda <= 0) {
					System.out.println("O valor de venda deve ser maior que zero!");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("Tipo inserido inválido. Digite um valor real!");
				valorVenda = 0;
			}
		} while (valorVenda <= 0);

		double valorCompra = 0;
		do {
			try {
				System.out.print("Valor de compra = ");
				valorCompra = scanner.nextDouble();
				scanner.nextLine();
				if (valorCompra <= 0) {
					System.out.println("O valor de compra deve ser maior que zero!");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("Tipo inserido inválido. Digite um valor real!");
				valorCompra = 0;
			}
		} while (valorCompra <= 0);

		if (valorVenda <= valorCompra) {
			System.out.println("Erro: O valor de venda deve ser maior que o valor de compra. Cadastro cancelado.");
			return;
		}

		int produtoIdGeradoNoBanco = -1;

		try (Connection conn = ConexaoBD.getConexao()) {
			System.out.println("Conexão com o banco de dados estabelecida");

			String sqlProduto = "INSERT INTO produto (Nome, ValorVenda, ValorCompra) VALUES (?, ?, ?)";

			try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto, Statement.RETURN_GENERATED_KEYS)) {
				stmtProduto.setString(1, nome);
				stmtProduto.setDouble(2, valorVenda);
				stmtProduto.setDouble(3, valorCompra);
				stmtProduto.executeUpdate();

				try (ResultSet rsGeneratedKeys = stmtProduto.getGeneratedKeys()) {
					if (rsGeneratedKeys.next()) {
						produtoIdGeradoNoBanco = rsGeneratedKeys.getInt(1);
						System.out.println("Produto cadastrado com sucesso! ID no banco: " + produtoIdGeradoNoBanco);
					} else {
						System.out.println("Erro: Não foi possível obter o ID gerado para o produto.");
						return;
					}
				}
			}

			int estoqueInicial = -1;
			do {
				try {
					System.out.print("Informe a quantidade inicial em estoque: ");
					estoqueInicial = scanner.nextInt();
					scanner.nextLine();
					if (estoqueInicial < 0) {
						System.out.println("A quantidade não pode ser negativa!");
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println("Tipo inserido inválido. Digite um número inteiro!");
					estoqueInicial = -1;
				}
			} while (estoqueInicial < 0);

			int estoqueMinimo = -1;
			do {
				try {
					System.out.print("Informe a quantidade mínima em estoque: ");
					estoqueMinimo = scanner.nextInt();
					scanner.nextLine();
					if (estoqueMinimo < 0) {
						System.out.println("A quantidade não pode ser negativa!");
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println("Tipo inserido inválido. Digite um número inteiro!");
					estoqueMinimo = -1;
				}
			} while (estoqueMinimo < 0);

			String sqlEstoque = "INSERT INTO estoque (Minimo, Estoque, Produto_Id) VALUES (?, ?, ?)";
			try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {
				stmtEstoque.setInt(1, estoqueMinimo);
				stmtEstoque.setInt(2, estoqueInicial);
				stmtEstoque.setInt(3, produtoIdGeradoNoBanco);
				stmtEstoque.executeUpdate();
				System.out.println("Estoque inicial do produto registrado com sucesso!");
			}

		} catch (SQLException e) {
			System.err.println("Erro ao cadastrar o produto ou estoque no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void listarProdutos() {
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT p.id, p.nome, p.valorVenda, p.valorCompra, e.estoque FROM Produto p JOIN Estoque e ON p.id = e.Produto_Id");
				ResultSet rs = stmt.executeQuery()) {

			boolean temProdutos = false;
			System.out.println("\n--- Lista de Produtos ---");
			while (rs.next()) {
				String idDoBanco = rs.getString("id");
				String nome = rs.getString("nome");
				double valorVenda = rs.getDouble("valorVenda");
				double valorCompra = rs.getDouble("valorCompra");
				int estoqueAtual = rs.getInt("estoque");

				try {
					Produto produto = new Produto(idDoBanco, nome, valorVenda, valorCompra);
					produto.setEstoqueProduto(new Estoque(estoqueAtual, 0, produto));
					System.out.println(produto);
					temProdutos = true;
				} catch (ProdutoException e) {
					System.err.println("Erro ao carregar produto ID " + idDoBanco + " do banco: " + e.getMessage());
				}
			}

			if (!temProdutos) {
				System.out.println("Não há produtos cadastrados no banco de dados.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erro ao consultar os produtos no banco de dados.");
		}
	}

	@Override
	public void atualizarProduto() {
		String procurarId;
		Produto produtoAtualizar = null;

		do {
			System.out.print("Informe o ID do produto a ser atualizado: ");
			procurarId = scanner.nextLine().trim();
			if (procurarId.isEmpty()) {
				System.out.println("O ID não pode ser vazio.");
				continue;
			}
			produtoAtualizar = buscarProdutoBanco(procurarId);

			if (produtoAtualizar == null) {
				System.out.println("Produto não encontrado.");
				return;
			}
		} while (produtoAtualizar == null);

		System.out.println("Produto encontrado: " + produtoAtualizar);

		System.out.print("Deseja atualizar o valor de venda? (S/N): ");
		char atualizarValorVendaChar = Character.toLowerCase(scanner.next().charAt(0));
		scanner.nextLine();
		if (atualizarValorVendaChar == 's') {
			double novoValorVenda = 0;
			boolean inputValido = false;
			do {
				try {
					System.out.print("Novo valor de venda: ");
					novoValorVenda = scanner.nextDouble();
					scanner.nextLine();
					if (novoValorVenda <= 0) {
						System.out.println("O valor de venda deve ser maior que zero!");
					} else if (novoValorVenda <= produtoAtualizar.getValorCompra()) {
						System.out.println("O valor de venda deve ser maior que o valor de compra atual ("
								+ produtoAtualizar.getValorCompra() + ")!");
					} else {
						inputValido = true;
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println("Tipo inserido inválido. Digite um valor real!");
					novoValorVenda = 0;
				}
			} while (!inputValido);

			try {
				produtoAtualizar.setValorVenda(novoValorVenda);
				atualizarProdutoBanco(produtoAtualizar.getId(), "ValorVenda", novoValorVenda);
			} catch (ProdutoException e) {
				System.err.println("Erro ao atualizar valor de venda: " + e.getMessage());
			}
		}

		System.out.print("Deseja atualizar o valor de compra? (S/N): ");
		char atualizarValorCompraChar = Character.toLowerCase(scanner.next().charAt(0));
		scanner.nextLine();
		if (atualizarValorCompraChar == 's') {
			double novoValorCompra = 0;
			boolean inputValido = false;
			do {
				try {
					System.out.print("Novo valor de compra: ");
					novoValorCompra = scanner.nextDouble();
					scanner.nextLine();
					if (novoValorCompra <= 0) {
						System.out.println("O valor de compra deve ser maior que zero!");
					} else if (produtoAtualizar.getValorVenda() <= novoValorCompra) {
						System.out.println("O valor de compra não pode ser maior ou igual ao valor de venda atual ("
								+ produtoAtualizar.getValorVenda() + ")!");
					} else {
						inputValido = true;
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println("Tipo inserido inválido. Digite um valor real!");
					novoValorCompra = 0;
				}
			} while (!inputValido);

			try {
				produtoAtualizar.setValorCompra(novoValorCompra);
				atualizarProdutoBanco(produtoAtualizar.getId(), "ValorCompra", novoValorCompra);
			} catch (ProdutoException e) {
				System.err.println("Erro ao atualizar valor de compra: " + e.getMessage());
			}
		}

		System.out.print("Deseja atualizar a descrição do produto? (S/N): ");
		char atualizarDescricaoChar = Character.toLowerCase(scanner.next().charAt(0));
		scanner.nextLine();
		if (atualizarDescricaoChar == 's') {
			System.out.print("Nova descrição: ");
			String novaDescricao = scanner.nextLine().trim();
			if (novaDescricao.isEmpty()) {
				System.out.println("Descrição não pode ser vazia. Atualização cancelada.");
			} else {
				try {
					produtoAtualizar.setDescricao(novaDescricao);
					atualizarProdutoBanco(produtoAtualizar.getId(), "Nome", novaDescricao);
				} catch (ProdutoException e) {
					System.err.println("Erro ao atualizar descrição: " + e.getMessage());
				}
			}
		}

		System.out.println("Produto atualizado com sucesso!");
	}

	private Produto buscarProdutoBanco(String id) {
		Produto produto = null;
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT id, Nome, ValorVenda, ValorCompra FROM produto WHERE id = ?")) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String idDoBanco = rs.getString("id");
					String nome = rs.getString("Nome");
					double valorVenda = rs.getDouble("ValorVenda");
					double valorCompra = rs.getDouble("ValorCompra");
					try {
						produto = new Produto(idDoBanco, nome, valorVenda, valorCompra);
					} catch (ProdutoException e) {
						System.err.println("Erro ao criar objeto Produto com dados do banco: " + e.getMessage());
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Erro ao buscar produto no banco de dados: " + e.getMessage());
		}
		return produto;
	}

	private void atualizarProdutoBanco(String id, String campo, Object novoValor) {
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement("UPDATE produto SET " + campo + " = ? WHERE id = ?")) {

			if (campo.equals("Nome")) {
				stmt.setString(1, (String) novoValor);
			} else if (campo.equals("ValorVenda") || campo.equals("ValorCompra")) {
				stmt.setDouble(1, (Double) novoValor);
			} else {
				System.out.println("Campo de atualização inválido: " + campo);
				return;
			}

			stmt.setString(2, id);
			stmt.executeUpdate();
			System.out.println("Atualização de " + campo + " no banco com sucesso!");

		} catch (SQLException e) {
			System.err.println(
					"Erro ao atualizar o produto no banco de dados para o campo " + campo + ": " + e.getMessage());
		}
	}

	@Override
	public void disponibilidadeProduto() {
		System.out.print("Informe a descrição do produto para verificar disponibilidade: ");
		String nomePesquisa = scanner.nextLine().trim();

		if (nomePesquisa.isEmpty()) {
			System.out.println("A descrição do produto não pode ser vazia.");
			return;
		}

		Produto produto = buscarProdutoPorDescricao(nomePesquisa);

		if (produto != null) {
			try (Connection conn = ConexaoBD.getConexao();
					PreparedStatement stmt = conn
							.prepareStatement("SELECT Estoque FROM estoque WHERE Produto_Id = ?")) {
				stmt.setString(1, produto.getId());
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						int quantidadeEstoque = rs.getInt("Estoque");
						System.out.println(
								"Produto encontrado: " + produto.getDescricao() + " (ID: " + produto.getId() + ")");
						System.out.println("Quantidade em estoque: " + quantidadeEstoque);
					} else {
						System.out
								.println("Informação de estoque não encontrada para o produto '" + nomePesquisa + "'.");
					}
				}
			} catch (SQLException e) {
				System.err.println("Erro ao buscar estoque no banco de dados: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("Produto com a descrição '" + nomePesquisa + "' não encontrado no banco de dados.");
		}
	}

	private Produto buscarProdutoPorDescricao(String nome) {
		Produto produto = null;
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT id, Nome, ValorVenda, ValorCompra FROM produto WHERE LOWER(Nome) = LOWER(?)")) {
			stmt.setString(1, nome);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String idDoBanco = rs.getString("id");
					String descricaoBanco = rs.getString("nome");
					double valorVenda = rs.getDouble("valorVenda");
					double valorCompra = rs.getDouble("valorCompra");

					try {
						produto = new Produto(idDoBanco, descricaoBanco, valorVenda, valorCompra);
					} catch (ProdutoException e) {
						System.err.println("Erro ao criar produto com os dados do banco de dados: " + e.getMessage());
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Erro ao buscar produto no banco de dados: " + e.getMessage());
		}
		return produto;
	}
}