package Gerenciadores;

import Classes.ConexaoBD;
import Classes.Funcionario;
import Classes.Salario;
import Classes.Setor;
import Classes.Transportadora;
import Enums.Regiao;
import Enums.Setores;
import Enums.Status;
import Exceptions.FuncionarioException;
import Interfaces.IntGestao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GerGestao implements IntGestao {

	private static final String MENSAGEM_ERRO_SQL = "Erro de SQL: ";
	private static final String MENSAGEM_ENTRADA_INVALIDA_NUMERO = "Entrada inválida! Digite um número.";
	private static final String MENSAGEM_ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente.";

	public GerGestao() {

	}

	@Override
	public void consultarNegocios() {
		boolean existemNegociosEmAndamento = false;
		Connection conexao = null;
		try {
			conexao = ConexaoBD.getConexao();

			System.out.println("\n--- Negócios em Andamento (Status 'ABERTO') ---");

			String sqlVendasAberto = "SELECT t.Id, t.Valor, t.Data, t.Funcionario_Id, t.Status, "
					+ "v.Transportadora_Id " + "FROM transacoes t " + "JOIN venda v ON t.Id = v.transacao_id "
					+ "WHERE t.Status = 'ABERTO'";
			try (PreparedStatement stmtVendas = conexao.prepareStatement(sqlVendasAberto);
					ResultSet rsVendas = stmtVendas.executeQuery()) {

				boolean vendasEncontradas = false;
				while (rsVendas.next()) {
					if (!vendasEncontradas) {
						System.out.println("\n--- Vendas em Andamento ---");
						vendasEncontradas = true;
						existemNegociosEmAndamento = true;
					}
					String idTransacaoVenda = String.valueOf(rsVendas.getInt("Id"));
					double valorVenda = rsVendas.getDouble("Valor");
					LocalDate dataVenda = rsVendas.getDate("Data").toLocalDate();
					String idFuncionarioVendaStr = String.valueOf(rsVendas.getInt("Funcionario_Id"));
					int idTransportadoraVenda = rsVendas.getInt("Transportadora_Id");

					// Busca funcionário e transportadora no banco de dados
					Funcionario funcionarioVenda = buscarFuncionarioNoBanco(idFuncionarioVendaStr);
					Transportadora transportadoraVenda = buscarTransportadoraNoBancoPorId(idTransportadoraVenda);

					System.out.printf("Venda #%s | Valor: R$%.2f | Data: %s | Func: %s | Transp: %s%n",
							idTransacaoVenda, valorVenda, dataVenda.toString(),
							(funcionarioVenda != null ? funcionarioVenda.getNome() : "N/A"),
							(transportadoraVenda != null ? transportadoraVenda.getNome() : "N/A"));
				}
				if (!vendasEncontradas) {
					System.out.println("Não há vendas em andamento.");
				}
			}
			String sqlComprasAberto = "SELECT t.Id, t.Valor, t.Data, t.Funcionario_Id, t.Status " + "FROM transacoes t "
					+ "JOIN compra c ON t.Id = c.transacao_id " + "WHERE t.Status = 'ABERTO'";
			try (PreparedStatement stmtCompras = conexao.prepareStatement(sqlComprasAberto);
					ResultSet rsCompras = stmtCompras.executeQuery()) {

				boolean comprasEncontradas = false;
				while (rsCompras.next()) {
					if (!comprasEncontradas) {
						System.out.println("\n--- Compras em Andamento ---");
						comprasEncontradas = true;
						existemNegociosEmAndamento = true;
					}
					String idTransacaoCompra = String.valueOf(rsCompras.getInt("Id"));
					double valorCompra = rsCompras.getDouble("Valor");
					LocalDate dataCompra = rsCompras.getDate("Data").toLocalDate();
					String idFuncionarioCompraStr = String.valueOf(rsCompras.getInt("Funcionario_Id"));

					// Busca funcionário no banco de dados
					Funcionario funcionarioCompra = buscarFuncionarioNoBanco(idFuncionarioCompraStr);

					System.out.printf("Compra #%s | Valor: R$%.2f | Data: %s | Func: %s%n", idTransacaoCompra,
							valorCompra, dataCompra.toString(),
							(funcionarioCompra != null ? funcionarioCompra.getNome() : "N/A"));
				}
				if (!comprasEncontradas) {
					System.out.println("Não há compras em andamento.");
				}
			}

			if (!existemNegociosEmAndamento) {
				System.out.println("\nNão há negócios em andamento (vendas ou compras).");
			}

		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao consultar negócios em andamento: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException excecaoFechamento) {
					System.err.println("Erro ao fechar conexão: " + excecaoFechamento.getMessage());
				}
			}
		}
	}

	@Override
	public void atualizarStatus(Scanner scanner) {
		Connection conexao = null;
		try {
			conexao = ConexaoBD.getConexao();
			conexao.setAutoCommit(false); // Inicia a transação para garantir atomicidade

			System.out.println("Insira o tipo de transação que deseja atualizar: ");
			System.out.println("1 - Compra");
			System.out.println("2 - Venda");
			int tipoTransacao = -1;
			try {
				tipoTransacao = scanner.nextInt();
				scanner.nextLine(); // Consome a nova linha após o nextInt()
			} catch (InputMismatchException e) {
				System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
				scanner.nextLine(); // Limpa o buffer
				conexao.rollback(); // Desfaz a transação em caso de erro de entrada
				return;
			}

			if (tipoTransacao != 1 && tipoTransacao != 2) {
				System.out.println("Opção inválida. Tente novamente.");
				conexao.rollback();
				return;
			}

			System.out.println("Insira o ID da transação que deseja atualizar: ");
			String idTransacaoString = scanner.nextLine();
			int idTransacaoNumerico;
			try {
				idTransacaoNumerico = Integer.parseInt(idTransacaoString); // Converte String para int
			} catch (NumberFormatException e) {
				System.err.println("ID da Transação inválido. Não é um número inteiro: " + idTransacaoString);
				conexao.rollback();
				return;
			}
			String sqlStatusAtual = "SELECT Status FROM transacoes WHERE Id = ?";
			Status statusAtual = null;
			try (PreparedStatement stmtStatusAtual = conexao.prepareStatement(sqlStatusAtual)) {
				stmtStatusAtual.setInt(1, idTransacaoNumerico);
				try (ResultSet rsStatus = stmtStatusAtual.executeQuery()) {
					if (rsStatus.next()) {
						try {
							statusAtual = Status.valueOf(rsStatus.getString("Status"));
						} catch (IllegalArgumentException e) {
							System.err.println("Erro: Status inválido no banco de dados para a transação ID "
									+ idTransacaoNumerico);
							conexao.rollback();
							return;
						}
					} else {
						System.out.println("Transação com ID " + idTransacaoNumerico + " não encontrada.");
						conexao.rollback();
						return;
					}
				}
			}

			if (statusAtual != Status.ABERTO) {
				System.out.println("Transação ID " + idTransacaoNumerico + " já está " + statusAtual.name()
						+ ". Apenas transações 'ABERTO' podem ser atualizadas.");
				conexao.rollback();
				return;
			}

			System.out.println("Escolha para qual status deseja atualizar: ");
			System.out.println("1 - CONCLUIR");
			System.out.println("2 - CANCELAR");
			int opcaoStatus = -1;
			try {
				opcaoStatus = scanner.nextInt();
				scanner.nextLine(); // Consome a nova linha
			} catch (InputMismatchException e) {
				System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
				scanner.nextLine();
				conexao.rollback();
				return;
			}

			Status novoStatus = null;
			if (opcaoStatus == 1) {
				novoStatus = Status.FECHADO;
			} else if (opcaoStatus == 2) {
				novoStatus = Status.CANCELADO;
			} else {
				System.out.println("Opção de status inválida. Tente novamente.");
				conexao.rollback();
				return;
			}

			String sqlAtualizarTransacao = "UPDATE transacoes SET Status = ? WHERE Id = ?";
			try (PreparedStatement stmtAtualizarTransacao = conexao.prepareStatement(sqlAtualizarTransacao)) {
				stmtAtualizarTransacao.setString(1, novoStatus.name());
				stmtAtualizarTransacao.setInt(2, idTransacaoNumerico);
				int linhasAfetadas = stmtAtualizarTransacao.executeUpdate();

				if (linhasAfetadas > 0) {
					conexao.commit();
					System.out.println("Status da transação ID " + idTransacaoNumerico + " atualizado para "
							+ novoStatus.name() + " com sucesso!");
				} else {
					conexao.rollback();
					System.out.println(
							"Falha ao atualizar o status da transação. ID não encontrado ou nenhum dado alterado.");
				}
			}

		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao atualizar status da transação: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexao != null)
					conexao.rollback();
			} catch (SQLException excecaoRollback) {
				System.err.println("Erro ao tentar rollback: " + excecaoRollback.getMessage());
			}
		} catch (Exception e) { // Catch para qualquer outra exceção inesperada
			System.err.println(MENSAGEM_ERRO_INESPERADO + "ao atualizar status: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexao != null)
					conexao.rollback();
			} catch (SQLException excecaoRollback) {
				System.err.println("Erro ao tentar rollback: " + excecaoRollback.getMessage());
			}
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException excecaoFechamento) {
					System.err.println("Erro ao fechar conexão: " + excecaoFechamento.getMessage());
				}
			}
		}
	}

	private static Funcionario buscarFuncionarioNoBanco(String idFuncionarioString) {
		Funcionario funcionario = null;
		try (Connection conexao = ConexaoBD.getConexao()) {
			int idFuncionarioNumerico;
			try {
				idFuncionarioNumerico = Integer.parseInt(idFuncionarioString);
			} catch (NumberFormatException e) {
				System.err.println("ID do Funcionário inválido. Não é um número inteiro: " + idFuncionarioString);
				return null;
			}

			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, "
					+ "s.id AS SetorId, s.nome AS SetorNome "
					+ "FROM funcionario f JOIN setor s ON f.Setor_Id = s.id WHERE f.id = ?";
			try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
				stmt.setInt(1, idFuncionarioNumerico);
				try (ResultSet resultado = stmt.executeQuery()) {
					if (resultado.next()) {
						String idDoBanco = String.valueOf(resultado.getInt("id"));
						String nome = resultado.getString("Nome");
						String cpf = resultado.getString("CPF");
						String genero = resultado.getString("Genero");
						double salarioBase = resultado.getDouble("SalarioBase");
						String nomeSetorStr = resultado.getString("SetorNome");

						Setores setorEnum = null;
						try {
							setorEnum = Setores.valueOf(nomeSetorStr.toUpperCase());
						} catch (IllegalArgumentException e) {
							System.err.println("Erro: Nome do setor '" + nomeSetorStr
									+ "' não corresponde a nenhum enum Setores para funcionário ID "
									+ idFuncionarioNumerico + ". Retornando null.");
							return null;
						}

						Setor setor = new Setor(setorEnum);

						try {
							funcionario = new Funcionario(idDoBanco, nome, cpf, genero, setor, null);
							Salario objetoSalario = new Salario(salarioBase, funcionario);
							funcionario.setSalario(objetoSalario);

						} catch (FuncionarioException e) {
							System.err.println("Erro ao criar objeto Funcionário a partir dos dados do banco (ID: "
									+ idDoBanco + "): " + e.getMessage());
							e.printStackTrace();
							funcionario = null;
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao buscar funcionário: " + e.getMessage());
			e.printStackTrace();
		}
		return funcionario;
	}

	private static Transportadora buscarTransportadoraNoBancoPorId(int idTransportadoraNumerico) {
		Transportadora transportadora = null;
		Connection conexao = null;
		try {
			conexao = ConexaoBD.getConexao();
			String sql = "SELECT Id, Nome, Cnpj, Taxa FROM transportadora WHERE Id = ?";
			try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
				stmt.setInt(1, idTransportadoraNumerico);
				try (ResultSet resultado = stmt.executeQuery()) {
					if (resultado.next()) {
						int idDoBanco = resultado.getInt("Id");
						String nome = resultado.getString("Nome");
						String cnpj = resultado.getString("Cnpj");
						double taxa = resultado.getDouble("Taxa");

						transportadora = new Transportadora(idDoBanco, nome, cnpj, taxa);

						List<Regiao> regioesAtendidas = carregarRegioesTransportadoraInterno(conexao, idDoBanco);
						for (Regiao regiao : regioesAtendidas) {
							transportadora.setRegiao(regiao);
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao buscar transportadora por ID: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException excecaoFechamento) {
					System.err.println("Erro ao fechar conexão: " + excecaoFechamento.getMessage());
				}
			}
		}
		return transportadora;
	}

	private static List<Regiao> carregarRegioesTransportadoraInterno(Connection conexao, int idTransportadora)
			throws SQLException {
		List<Regiao> regioes = new ArrayList<>();
		String sql = "SELECT regiao_id FROM transportadora_regiao WHERE transportadora_id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idTransportadora);
			try (ResultSet resultado = stmt.executeQuery()) {
				while (resultado.next()) {
					int indiceEnumRegiao = resultado.getInt("regiao_id") - 1;
					if (indiceEnumRegiao >= 0 && indiceEnumRegiao < Regiao.values().length) {
						regioes.add(Regiao.values()[indiceEnumRegiao]);
					} else {
						System.err.println("Aviso: Valor de Região inválido (" + (indiceEnumRegiao + 1)
								+ ") encontrado no DB para transportadora ID " + idTransportadora);
					}
				}
			}
		}
		return regioes;
	}
} 