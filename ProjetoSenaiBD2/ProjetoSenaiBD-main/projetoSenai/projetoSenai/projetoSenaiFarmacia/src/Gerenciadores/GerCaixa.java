package Gerenciadores;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import Classes.*;
import Enums.Regiao;
import Interfaces.IntCaixa;
import Enums.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Exceptions.ProdutoException;
import Exceptions.FuncionarioException;
import Enums.Setores;

public class GerCaixa implements IntCaixa {
	Scanner scanner = new Scanner(System.in);

	private static final String MSG_ERRO_SQL = "Erro de SQL: ";
	private static final String MSG_ENTRADA_INVALIDA_NUMERO = "Entrada inválida! Digite um número inteiro.";
	private static final String MSG_ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente.";
	private static final String MSG_ID_INVALIDO_NUMERO = "ID inválido. Não é um número inteiro: ";
	private static final String MSG_FALHA_OBTER_ID = "Erro: Não foi possível obter o ID gerado para ";

	public void menu() {
		System.out.println("Escolha uma das opções: ");
		System.out.println("--------------------------------------------");
		System.out.println("| 1 - Registrar entrada                    |");
		System.out.println("| 2 - Registrar saída                      |");
		System.out.println("| 3 - Visualizar saldo atual               |");
		System.out.println("| 4 - Verificar lucro mensal               |");
		System.out.println("| 5 - Verificar lucro anual                |");
		System.out.println("| 6 - Gerar relatório                      |");
		System.out.println("| 0 - Sair                                 |");
		System.out.println("--------------------------------------------");
		System.out.println();
	}

	public void registrarEntrada(Caixa caixa, Venda novaVenda, ArrayList<Funcionario> funcionarios,
			ArrayList<Transportadora> transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos) {

		novaVenda = realizarVenda(scanner, funcionarios, transportadoras, setores);
		if (novaVenda != null) {
			boolean vendaSalva = salvarVendaNoBanco(novaVenda);
			if (vendaSalva) {
				caixa.getEntrada().add(novaVenda);
				if (novaVenda.getStatus().equals(Status.FECHADO))
					caixa.setSaldo(caixa.getSaldo() + novaVenda.getValor());
				System.out.println("Venda registrada com sucesso no sistema e no banco de dados!");
			} else {
				System.err.println("Erro: Falha ao registrar venda no banco de dados.");
			}
		} else {
			System.out.println("Registro de entrada cancelado ou falhou.");
		}
	}

	public void registrarSaida(Caixa caixa, Compra novaCompra, ArrayList<Funcionario> funcionarios,
			ArrayList<Produto> produtos) {
		novaCompra = realizarCompra(scanner, funcionarios);
		if (novaCompra != null) {
			boolean compraSalva = salvarCompraNoBanco(novaCompra);
			if (compraSalva) {
				caixa.getSaida().add(novaCompra);
				if (novaCompra.getStatus().equals(Status.FECHADO))
					caixa.setSaldo(caixa.getSaldo() - novaCompra.getValor());
				System.out.println("Compra registrada com sucesso no sistema e no banco de dados!");
			} else {
				System.err.println("Erro: Falha ao registrar compra no banco de dados.");
			}
		} else {
			System.out.println("Registro de saída cancelado ou falhou.");
		}
	}

	public static Venda realizarVenda(Scanner scanner, ArrayList<Funcionario> funcionarios,
			ArrayList<Transportadora> transportadoras, ArrayList<Setor> setores) {

		Venda venda = new Venda();
		Funcionario funcionarioVenda = null;
		Transportadora transportadora = null;
		ArrayList<Itens> itens = new ArrayList<>();

		System.out.println("Iniciando uma nova venda.");

		String prodId;
		do {
			System.out.print("Insira o ID do produto ou 0 para parar: ");
			prodId = scanner.next();
			scanner.nextLine();

			if (!prodId.equals("0")) {
				Produto produto = buscarProdutoNoBanco(prodId);

				if (produto != null) {
					int quantidade = -1;
					do {
						try {
							System.out.println("Quantas unidades do produto " + produto.getDescricao()
									+ " deseja vender? (0 para cancelar)");
							quantidade = scanner.nextInt();
							scanner.nextLine();
							if (quantidade < 0) {
								System.out.println("Quantidade não pode ser negativa!");
								quantidade = -1;
							}
						} catch (InputMismatchException e) {
							System.out.println(MSG_ENTRADA_INVALIDA_NUMERO);
							scanner.nextLine();
							quantidade = -1;
						}
					} while (quantidade == -1);

					if (quantidade > 0) {
						if (produto.verificaEstoque(quantidade)) {
							produto.getEstoqueProduto().realizaTransacao(-quantidade);
							boolean estoqueAtualizadoDB = atualizarEstoqueProdutoNoBanco(produto.getId(),
									produto.getEstoqueProduto().getEstoque());
							if (!estoqueAtualizadoDB) {
								System.err.println("Aviso: Falha ao atualizar estoque do produto " + produto.getId()
										+ " no banco de dados. Venda pode ser inconsistente.");
							}
							Itens item = new Itens(quantidade, produto);
							itens.add(item);
						} else {
							System.out.println("Quantidade excede a disponivel em estoque!");
						}
					} else if (quantidade == 0) {
						System.out.println("Produto não adicionado à venda.");
					}
				} else {
					System.out.println("Produto com ID " + prodId + " não encontrado.");
				}
			}
		} while (!prodId.equals("0"));

		if (itens.isEmpty()) {
			System.out.println("Nenhum produto foi adicionado à venda. Venda cancelada.");
			return null;
		}

		venda.setProdutos(itens);

		String idFuncionario; 
		boolean cancelarFuncionario = false;
		do {
			System.out.print("Informe o ID do funcionário (ou digite '0' para voltar): ");
			idFuncionario = scanner.next();
			scanner.nextLine();

			if (idFuncionario.equals("0")) {
				System.out.println("Seleção de funcionário cancelada. Venda cancelada.");
				cancelarFuncionario = true;
				break; // Sai do loop
			}

			funcionarioVenda = buscarFuncionarioNoBanco(idFuncionario);

			if (funcionarioVenda != null) {
				venda.setFuncionario(funcionarioVenda);
			} else {
				System.out.println("\nFuncionário com ID " + idFuncionario + " não encontrado!\n");
			}
		} while (funcionarioVenda == null && !cancelarFuncionario); 

		if (cancelarFuncionario) { 
			return null; 
		} 

		LocalDate dtVenda = solicitarData(scanner);
		if (dtVenda == null) {
			System.out.println("Data da venda inválida. Venda cancelada.");
			return null;
		}
		venda.setData(dtVenda);

		Regiao regiao = solicitarRegiao(scanner);
		if (regiao == null) {
			System.out.println("Seleção de região inválida. Venda cancelada.");
			return null;
		}

		ArrayList<Transportadora> transportadorasDoBanco = buscarTransportadorasNoBanco();

		boolean transportadoraValidaSelecionada = false;
		do {
			if (transportadorasDoBanco.isEmpty()) {
				System.out.println("Não há transportadoras cadastradas no banco de dados.");
				System.out.println("Retornando ao menu anterior.");
				return null;
			}

			try {
				System.out.println("Escolha a transportadora para a venda (digite 0 para voltar):");
				for (int i = 0; i < transportadorasDoBanco.size(); i++) {
					System.out.println((i + 1) + " - " + transportadorasDoBanco.get(i).getNome() + " (ID DB: "
							+ transportadorasDoBanco.get(i).getIdDB() + ", Taxa: "
							+ String.format("%.2f", transportadorasDoBanco.get(i).getTaxa()) + "%)");
				}
				int escolhaUsuario = scanner.nextInt();
				scanner.nextLine();

				if (escolhaUsuario == 0) {
					System.out.println("Seleção de transportadora cancelada. Venda cancelada.");
					return null;
				}

				int transportadoraEscolhida = escolhaUsuario - 1;

				if (transportadoraEscolhida >= 0 && transportadoraEscolhida < transportadorasDoBanco.size()) {
					Transportadora tempTransportadora = transportadorasDoBanco.get(transportadoraEscolhida);
					if (tempTransportadora.atendeRegiao(regiao)) {
						transportadora = tempTransportadora;
						transportadoraValidaSelecionada = true;
					} else {
						System.out.println("Transportadora '" + tempTransportadora.getNome() + "' não atende a região "
								+ regiao + "! Por favor, escolha outra ou digite 0 para voltar.");
					}
				} else {
					System.out.println(
							"Opção inválida! Número fora do alcance. Por favor, escolha outra ou digite 0 para voltar.");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println(
						MSG_ENTRADA_INVALIDA_NUMERO + " Por favor, escolha um número válido ou digite 0 para voltar.");
			} catch (Exception e) {
				scanner.nextLine();
				System.out.println(MSG_ERRO_INESPERADO
						+ " na seleção da transportadora. Tente novamente ou digite 0 para voltar.");
			}
		} while (!transportadoraValidaSelecionada);

		if (transportadora == null) {
			System.out.println("Nenhuma transportadora válida foi selecionada. Venda não pode prosseguir.");
			return null;
		}

		venda.setTransportadora(transportadora);
		venda.setValor(venda.calculaTotal());

		if (dtVenda.isAfter(LocalDate.now())) {
			venda.setStatus(Status.ABERTO);
		} else {
			venda.setStatus(Status.FECHADO);
		}

		return venda;
	}

	public static Compra realizarCompra(Scanner scanner, ArrayList<Funcionario> funcionarios) {
		Compra compra = new Compra();
		Funcionario funcionarioCompra = null;
		ArrayList<Itens> itens = new ArrayList<>();

		System.out.println("Iniciando nova compra...");

		String prodId;
		int contador = 0;
		do {
			System.out.println(contador == 0 ? "Insira o ID do produto ou 0 para cancelar a compra: "
					: "Insira o ID do novo produto ou 0 para finalizar a compra: ");
			prodId = scanner.next();
			scanner.nextLine();

			if (!prodId.equals("0")) {
				Produto produto = buscarProdutoNoBanco(prodId);

				if (produto != null) {
					int quantidade = -1;
					do {
						try {
							System.out.println("Quantas unidades do produto " + produto.getDescricao()
									+ " deseja comprar? (0 para cancelar)");
							quantidade = scanner.nextInt();
							scanner.nextLine();
							if (quantidade < 0) {
								System.out.println("Quantidade não pode ser negativa!");
								quantidade = -1;
							}
						} catch (InputMismatchException e) {
							System.out.println(MSG_ENTRADA_INVALIDA_NUMERO);
							scanner.nextLine();
							quantidade = -1;
						}
					} while (quantidade == -1);

					if (quantidade > 0) {
						Itens item = new Itens(quantidade, produto);
						itens.add(item);
						produto.getEstoqueProduto().realizaTransacao(quantidade);
						boolean estoqueAtualizadoDB = atualizarEstoqueProdutoNoBanco(produto.getId(),
								produto.getEstoqueProduto().getEstoque());
						if (!estoqueAtualizadoDB) {
							System.err.println("Aviso: Falha ao atualizar estoque do produto " + produto.getId()
									+ " no banco de dados. Compra pode ser inconsistente.");
						}
					} else if (quantidade == 0) {
						System.out.println("Produto não adicionado à compra.");
					}
				} else {
					System.out.println("Produto com ID " + prodId + " não encontrado.");
				}
			}
			contador = 1;
		} while (!prodId.equals("0"));

		if (itens.isEmpty()) {
			System.out.println("Nenhum produto foi adicionado à compra. Compra cancelada!");
			return null;
		}

		compra.setProdutos(itens);

		do {
			System.out.print("Informe o ID do funcionário: ");
			String idFuncionario = scanner.next();
			scanner.nextLine();
			funcionarioCompra = buscarFuncionarioNoBanco(idFuncionario);

			if (funcionarioCompra != null) {
				compra.setFuncionario(funcionarioCompra);
			} else {
				System.out.println("\nFuncionário com ID " + idFuncionario + " não encontrado !\n");
			}
		} while (funcionarioCompra == null);

		LocalDate dtCompra = solicitarData(scanner);
		if (dtCompra == null) {
			System.out.println("Data da compra inválida. Compra cancelada.");
			return null;
		}
		compra.setData(dtCompra);

		if (dtCompra.isAfter(LocalDate.now())) {
			compra.setStatus(Status.ABERTO);
		} else {
			compra.setStatus(Status.FECHADO);
		}

		compra.setValor(compra.calculaTotal());

		return compra;
	}

	private static Produto buscarProdutoNoBanco(String idProdutoString) {
		Produto produto = null;
		try (Connection conn = ConexaoBD.getConexao()) {
			int idProdutoInt = parseId(idProdutoString, "Produto"); 
			if (idProdutoInt == -1)
				return null;

			String sql = "SELECT p.Id, p.Nome, p.ValorVenda, p.ValorCompra, e.Estoque, e.Minimo "
					+ "FROM produto p JOIN estoque e ON p.Id = e.Produto_Id WHERE p.Id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idProdutoInt);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				System.out.println();
				System.out.println("Produto Encontrado!");
				String idDoBanco = String.valueOf(rs.getInt("Id"));
				String descricao = rs.getString("Nome");
				double valorVenda = rs.getDouble("ValorVenda");
				double valorCompra = rs.getDouble("ValorCompra");
				int estoqueAtual = rs.getInt("Estoque");
				int estoqueMinimo = rs.getInt("Minimo");

				try {
					produto = new Produto(idDoBanco, descricao, valorVenda, valorCompra);
					Estoque estoqueObj = new Estoque(estoqueAtual, estoqueMinimo, produto);
					produto.setEstoqueProduto(estoqueObj);
				} catch (ProdutoException e) {
					System.err.println("Erro ao criar objeto Produto a partir dos dados do banco: " + e.getMessage());
					e.printStackTrace();
					produto = null;
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao buscar produto: " + e.getMessage());
			e.printStackTrace();
		}
		return produto;
	}

	private static Funcionario buscarFuncionarioNoBanco(String idFuncionarioString) {
		Funcionario funcionario = null;
		try (Connection conn = ConexaoBD.getConexao()) {
			int idFuncionarioInt = parseId(idFuncionarioString, "Funcionário");
			if (idFuncionarioInt == -1)
				return null;

			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, "
					+ "s.id AS SetorId, s.nome AS SetorNome "
					+ "FROM funcionario f JOIN setor s ON f.Setor_Id = s.id WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idFuncionarioInt);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String idDoBanco = String.valueOf(rs.getInt("id"));
				String nome = rs.getString("Nome");
				String cpf = rs.getString("CPF");
				String genero = rs.getString("Genero");
				double salarioBase = rs.getDouble("SalarioBase");
				String setorNomeStr = rs.getString("SetorNome");

				Setores setorEnum = null;
				try {
					setorEnum = Setores.valueOf(setorNomeStr.toUpperCase());
				} catch (IllegalArgumentException e) {
					System.err.println("Erro: Nome do setor '" + setorNomeStr
							+ "' não corresponde a nenhum enum Setores para funcionário ID " + idFuncionarioInt
							+ ". Retornando null.");
					return null;
				}

				Setor setor = new Setor(setorEnum);

				try {
					funcionario = new Funcionario(idDoBanco, nome, cpf, genero, setor, null);
					Salario salarioObj = new Salario(salarioBase, funcionario);
					funcionario.setSalario(salarioObj);

				} catch (FuncionarioException e) {
					System.err.println("Erro ao criar objeto Funcionário a partir dos dados do banco (ID: " + idDoBanco
							+ "): " + e.getMessage());
					e.printStackTrace();
					funcionario = null;
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao buscar funcionário: " + e.getMessage());
			e.printStackTrace();
		}
		return funcionario;
	}

	private static ArrayList<Transportadora> buscarTransportadorasNoBanco() {
		ArrayList<Transportadora> transportadoras = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();
			String sql = "SELECT Id, Nome, Cnpj, Taxa FROM transportadora";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int idDoBanco = rs.getInt("Id");
				String nome = rs.getString("Nome");
				String cnpj = rs.getString("Cnpj");
				double taxa = rs.getDouble("Taxa");

				Transportadora transportadora = new Transportadora(idDoBanco, nome, cnpj, taxa);

				List<Regiao> regioesAtendidas = carregarRegioesTransportadoraInterno(conn, idDoBanco);
				for (Regiao r : regioesAtendidas) {
					transportadora.setRegiao(r);
				}

				transportadoras.add(transportadora);
			}
		} catch (SQLException e) {
			System.err.println("Erro de SQL ao buscar transportadoras: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
			}
		}
		return transportadoras;
	}

	private static boolean atualizarEstoqueProdutoNoBanco(String idProdutoString, int novoEstoque) {
		int idProdutoInt = parseId(idProdutoString, "Produto");
		if (idProdutoInt == -1)
			return false;

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "UPDATE estoque SET Estoque = ? WHERE Produto_Id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, novoEstoque);
			stmt.setInt(2, idProdutoInt);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao atualizar estoque do produto no banco de dados: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private boolean salvarCompraNoBanco(Compra compra) {
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();
			conn.setAutoCommit(false);

			String sqlTransacoes = "INSERT INTO transacoes (Valor, Data, Funcionario_Id, Status) VALUES (?, ?, ?, ?)";
			PreparedStatement stmtTransacoes = conn.prepareStatement(sqlTransacoes, Statement.RETURN_GENERATED_KEYS);

			Objects.requireNonNull(compra.getFuncionario(),
					"Funcionário da compra não pode ser nulo ao salvar transação.");
			Objects.requireNonNull(compra.getStatus(), "Status da compra não pode ser nulo ao salvar transação.");

			stmtTransacoes.setDouble(1, compra.getValor());
			stmtTransacoes.setDate(2, java.sql.Date.valueOf(compra.getData()));
			stmtTransacoes.setInt(3, parseId(compra.getFuncionario().getId(), "Funcionário da Transação"));
			stmtTransacoes.setString(4, compra.getStatus().name());
			stmtTransacoes.executeUpdate();

			ResultSet rsTransacoesKeys = stmtTransacoes.getGeneratedKeys();
			int transacaoIdGerado = -1;
			if (rsTransacoesKeys.next()) {
				transacaoIdGerado = rsTransacoesKeys.getInt(1);

			} else {
				System.err.println(MSG_FALHA_OBTER_ID + "transação de compra.");
				conn.rollback();
				return false;
			}

			String sqlCompra = "INSERT INTO compra (Valor, Data, Funcionario_Id, Status, transacao_id) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
			stmtCompra.setDouble(1, compra.getValor());
			stmtCompra.setDate(2, java.sql.Date.valueOf(compra.getData()));
			stmtCompra.setInt(3, parseId(compra.getFuncionario().getId(), "Funcionário da Compra"));
			stmtCompra.setString(4, compra.getStatus().name());
			stmtCompra.setInt(5, transacaoIdGerado);

			stmtCompra.executeUpdate();

			ResultSet rsCompraKeys = stmtCompra.getGeneratedKeys();
			if (rsCompraKeys.next()) {
				String compraIdGerada = String.valueOf(rsCompraKeys.getInt(1));
				compra.setId(compraIdGerada);
			} else {
				System.err.println(MSG_FALHA_OBTER_ID + "compra.");
				conn.rollback();
				return false;
			}

			String sqlItensCompra = "INSERT INTO itens_compra (Compra_Id, Produto_Id, Quantidade, ValorUnitarioNaCompra) VALUES (?, ?, ?, ?)";
			PreparedStatement stmtItensCompra = conn.prepareStatement(sqlItensCompra);

			Objects.requireNonNull(compra.getProdutos(), "Lista de produtos da compra não pode ser nula.");
			for (Itens item : compra.getProdutos()) {
				Objects.requireNonNull(item, "Item da compra não pode ser nulo.");
				Objects.requireNonNull(item.getProduto(), "Produto do item da compra não pode ser nulo.");

				stmtItensCompra.setInt(1, parseId(compra.getId(), "ID da Compra para Itens"));
				stmtItensCompra.setInt(2, parseId(item.getProduto().getId(), "Produto do Item da Compra"));
				stmtItensCompra.setInt(3, item.getQuantidade());
				stmtItensCompra.setDouble(4, item.getProduto().getValorCompra());
				stmtItensCompra.addBatch();
			}
			stmtItensCompra.executeBatch();
			conn.commit();
			return true;
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao salvar compra no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} catch (NumberFormatException e) {
			System.err.println("Erro de formato de ID (Funcionário/Produto) ao salvar compra: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} catch (NullPointerException e) {
			System.err.println("Erro: Objeto nulo encontrado ao salvar compra: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão no finally: " + closeEx.getMessage());
				}
			}
		}
	}

	private boolean salvarVendaNoBanco(Venda venda) {
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();
			conn.setAutoCommit(false);

			String sqlTransacoes = "INSERT INTO transacoes (Valor, Data, Funcionario_Id, Status) VALUES (?, ?, ?, ?)";
			PreparedStatement stmtTransacoes = conn.prepareStatement(sqlTransacoes, Statement.RETURN_GENERATED_KEYS);

			Objects.requireNonNull(venda.getFuncionario(),
					"Funcionário da venda não pode ser nulo ao salvar transação.");
			Objects.requireNonNull(venda.getStatus(), "Status da venda não pode ser nulo ao salvar transação.");

			stmtTransacoes.setDouble(1, venda.getValor());
			stmtTransacoes.setDate(2, java.sql.Date.valueOf(venda.getData()));
			stmtTransacoes.setInt(3, parseId(venda.getFuncionario().getId(), "Funcionário da Transação"));
			stmtTransacoes.setString(4, venda.getStatus().name());
			stmtTransacoes.executeUpdate();

			ResultSet rsTransacoesKeys = stmtTransacoes.getGeneratedKeys();
			int transacaoIdGerado = -1;
			if (rsTransacoesKeys.next()) {
				transacaoIdGerado = rsTransacoesKeys.getInt(1);

			} else {
				System.err.println(MSG_FALHA_OBTER_ID + "transação de venda.");
				conn.rollback();
				return false;
			}

			String sqlVenda = "INSERT INTO venda (Valor, Data, Funcionario_Id, Status, Transportadora_Id, transacao_id) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS); // Mantenha
																											// RETURN_GENERATED_KEYS
																											// se 'Id' é
																											// autoincrementado

			stmtVenda.setDouble(1, venda.getValor());
			stmtVenda.setDate(2, java.sql.Date.valueOf(venda.getData()));
			stmtVenda.setInt(3, parseId(venda.getFuncionario().getId(), "Funcionário da Venda"));
			stmtVenda.setString(4, venda.getStatus().name());

			Objects.requireNonNull(venda.getTransportadora(), "Transportadora da venda não pode ser nula ao salvar.");
			stmtVenda.setInt(5, venda.getIdTransportadoraDB());
			stmtVenda.setInt(6, transacaoIdGerado); // Chave estrangeira para transacoes

			stmtVenda.executeUpdate();

			ResultSet rsVendaKeys = stmtVenda.getGeneratedKeys();
			if (rsVendaKeys.next()) {
				String vendaIdGerada = String.valueOf(rsVendaKeys.getInt(1));
				venda.setId(vendaIdGerada); // Atualiza o ID do objeto Venda com o ID da tabela 'venda'
			} else {
				System.err.println(MSG_FALHA_OBTER_ID + "venda.");
				conn.rollback();
				return false;
			}

			String sqlItensVenda = "INSERT INTO itens_venda (Venda_Id, Produto_Id, Quantidade, ValorUnitarioNaVenda) VALUES (?, ?, ?, ?)";
			PreparedStatement stmtItensVenda = conn.prepareStatement(sqlItensVenda);

			Objects.requireNonNull(venda.getProdutos(), "Lista de produtos da venda não pode ser nula.");
			for (Itens item : venda.getProdutos()) {
				Objects.requireNonNull(item, "Item da venda não pode ser nulo.");
				Objects.requireNonNull(item.getProduto(), "Produto do item da venda não pode ser nulo.");

				stmtItensVenda.setInt(1, parseId(venda.getId(), "ID da Venda para Itens"));
				stmtItensVenda.setInt(2, parseId(item.getProduto().getId(), "Produto do Item da Venda"));
				stmtItensVenda.setInt(3, item.getQuantidade());
				stmtItensVenda.setDouble(4, item.getProduto().getValorVenda());
				stmtItensVenda.addBatch();
			}
			stmtItensVenda.executeBatch();
			conn.commit();
			return true;
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao salvar venda no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} catch (NumberFormatException e) {
			System.err.println(
					"Erro de formato de ID (Funcionário/Transportadora/Produto) ao salvar venda: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} catch (NullPointerException e) {
			System.err.println("Erro: Objeto nulo encontrado ao salvar venda: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão no finally: " + closeEx.getMessage());
				}
			}
		}
	}

	public static int parseId(String idString, String tipoObjeto) {
		if (idString == null || idString.trim().isEmpty()) {
			System.err.println("Erro: ID de " + tipoObjeto + " é nulo ou vazio e não pode ser convertido para número.");
			return -1;
		}
		try {
			return Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			System.err.println(MSG_ID_INVALIDO_NUMERO + idString + " para " + tipoObjeto + ". " + e.getMessage());
			return -1;
		}
	}

	private static LocalDate solicitarData(Scanner scanner) {
		LocalDate data = null;
		do {
			try {
				System.out.println("Digite a data (AAAA-MM-DD) ou HJ para dia de hoje: ");
				String dataInput = scanner.nextLine();

				if (dataInput.equalsIgnoreCase("HJ")) {
					data = LocalDate.now();
				} else {
					data = LocalDate.parse(dataInput);
				}
			} catch (DateTimeParseException e) {
				System.out.println("Formato de data inválido! Use AAAA-MM-DD ou HJ.");
				data = null;
			}
		} while (data == null);
		return data;
	}

	private static Regiao solicitarRegiao(Scanner scanner) {
		Regiao regiao = null;
		do {
			try {
				System.out.println("Escolha a região de entrega:");
				Regiao[] regioes = Regiao.values();
				for (int i = 0; i < regioes.length; i++) {
					System.out.println((i + 1) + " - " + regioes[i]);
				}
				int regiaoEscolhida = scanner.nextInt();
				scanner.nextLine();
				if (regiaoEscolhida > 0 && regiaoEscolhida <= regioes.length) {
					regiao = regioes[regiaoEscolhida - 1];
				} else {
					System.out.println("Opção de região inválida!");
					regiao = null;
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println(MSG_ENTRADA_INVALIDA_NUMERO);
				regiao = null;
			} catch (Exception e) {
				scanner.nextLine();
				System.out.println(MSG_ERRO_INESPERADO + " na seleção de região. Tente novamente.");
				regiao = null;
			}
		} while (regiao == null);
		return regiao;
	}
	
	@Override
	public void visualizarSaldoAtual(Caixa caixa) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Saldo atual: R$ " + String.format("%.2f", caixa.totalCaixa()));
	}

	public void verificarLucroMensal(Caixa caixa) {
		System.out.print("Informe o mês (1-12): ");
		int mes = scanner.nextInt();
		System.out.print("Informe o ano: ");
		int ano = scanner.nextInt();
		scanner.nextLine();

		System.out.println(
				"Lucro mensal (" + mes + "/" + ano + "): R$ " + String.format("%.2f", caixa.lucroMensal(mes, ano)));
	}

	public double calcularSaldoAtualNoBanco() {
		double saldoTotal = 0.0;
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();

			String sqlVendasFechadas = "SELECT SUM(t.Valor) AS TotalVendas FROM transacoes t "
					+ "JOIN venda v ON t.Id = v.transacao_id " + "WHERE t.Status = 'FECHADO'";
			PreparedStatement stmtVendas = conn.prepareStatement(sqlVendasFechadas);
			ResultSet rsVendas = stmtVendas.executeQuery();
			if (rsVendas.next()) {
				saldoTotal += rsVendas.getDouble("TotalVendas");
			}

			String sqlComprasFechadas = "SELECT SUM(t.Valor) AS TotalCompras FROM transacoes t "
					+ "JOIN compra c ON t.Id = c.transacao_id " + "WHERE t.Status = 'FECHADO'";
			PreparedStatement stmtCompras = conn.prepareStatement(sqlComprasFechadas);
			ResultSet rsCompras = stmtCompras.executeQuery();
			if (rsCompras.next()) {
				saldoTotal -= rsCompras.getDouble("TotalCompras"); 
			}

		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao calcular saldo atual: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
				}
			}
		}
		return saldoTotal;
	}

	public double calcularLucroMensalNoBanco(int mes, int ano) {
		double lucroMensal = 0.0;
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();

			String sqlVendasMes = "SELECT SUM(t.Valor) AS TotalVendas FROM transacoes t "
					+ "JOIN venda v ON t.Id = v.transacao_id "
					+ "WHERE YEAR(t.Data) = ? AND MONTH(t.Data) = ? AND t.Status = 'FECHADO'";
			PreparedStatement stmtVendas = conn.prepareStatement(sqlVendasMes);
			stmtVendas.setInt(1, ano);
			stmtVendas.setInt(2, mes);
			ResultSet rsVendas = stmtVendas.executeQuery();
			if (rsVendas.next()) {
				lucroMensal += rsVendas.getDouble("TotalVendas");
			}

			String sqlComprasMes = "SELECT SUM(t.Valor) AS TotalCompras FROM transacoes t "
					+ "JOIN compra c ON t.Id = c.transacao_id "
					+ "WHERE YEAR(t.Data) = ? AND MONTH(t.Data) = ? AND t.Status = 'FECHADO'";
			PreparedStatement stmtCompras = conn.prepareStatement(sqlComprasMes);
			stmtCompras.setInt(1, ano);
			stmtCompras.setInt(2, mes);
			ResultSet rsCompras = stmtCompras.executeQuery();
			if (rsCompras.next()) {
				lucroMensal -= rsCompras.getDouble("TotalCompras"); 
			}

		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao calcular lucro mensal: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
				}
			}
		}
		return lucroMensal;
	}

	public double calcularLucroAnualNoBanco(int ano) {
		double lucroAnual = 0.0;
		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();

			String sqlVendasAno = "SELECT SUM(t.Valor) AS TotalVendas FROM transacoes t "
					+ "JOIN venda v ON t.Id = v.transacao_id " + "WHERE YEAR(t.Data) = ? AND t.Status = 'FECHADO'";
			PreparedStatement stmtVendas = conn.prepareStatement(sqlVendasAno);
			stmtVendas.setString(1, String.valueOf(ano));
			ResultSet rsVendas = stmtVendas.executeQuery();
			if (rsVendas.next()) {
				lucroAnual += rsVendas.getDouble("TotalVendas");
			}

			String sqlComprasAno = "SELECT SUM(t.Valor) AS TotalCompras FROM transacoes t "
					+ "JOIN compra c ON t.Id = c.transacao_id " + "WHERE YEAR(t.Data) = ? AND t.Status = 'FECHADO'";
			PreparedStatement stmtCompras = conn.prepareStatement(sqlComprasAno);
			stmtCompras.setString(1, String.valueOf(ano));
			ResultSet rsCompras = stmtCompras.executeQuery();
			if (rsCompras.next()) {
				lucroAnual -= rsCompras.getDouble("TotalCompras"); 
			}

		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao calcular lucro anual: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
				}
			}
		}
		return lucroAnual;
	}

	public void verificarLucroAnual(Caixa caixa) {
		System.out.print("Informe o ano: ");
		int ano = scanner.nextInt();
		scanner.nextLine();

		System.out.println("Lucro anual (" + ano + "): R$ " + String.format("%.2f", caixa.lucroAnual(ano)));
	}

	public void gerarRelatorio(Caixa caixa) {
		System.out.println("\n--- Relatório de Caixa ---");
		carregarTransacoesDoBanco(caixa);

		System.out.println("Saldo Atual: R$ " + String.format("%.2f", caixa.totalCaixa()));

		double totalVendas = 0.0;
		System.out.println("\n--- Transações de Entrada (Vendas) ---");
		if (caixa.getEntrada().isEmpty()) {
			System.out.println("  Nenhuma venda registrada.");
		} else {
			for (Venda venda : caixa.getEntrada()) {
				String funcInfo = (venda.getFuncionario() != null) ? "Funcionário: " + venda.getFuncionario().getNome()
						: "Funcionário: N/A";
				String transpInfo = (venda.getTransportadora() != null)
						? "Transportadora: " + venda.getTransportadora().getNome()
						: "Transportadora: N/A";
				System.out.println("  ID: " + venda.getId() + ", Data: " + venda.getData() + ", Valor: R$ "
						+ String.format("%.2f", venda.getValor()) + ", Status: " + venda.getStatus() + ", " + funcInfo
						+ ", " + transpInfo);

				if (venda.getProdutos() != null && !venda.getProdutos().isEmpty()) {
					System.out.println("    Itens da Venda:");
					for (Itens item : venda.getProdutos()) {
						System.out.println("      - Produto: " + item.getProduto().getDescricao() + ", Qtd: "
								+ item.getQuantidade() + ", Valor Unit.: R$ "
								+ String.format("%.2f", item.getProduto().getValorVenda()));
					}
				}
				totalVendas += venda.getValor();
			}

			System.out.println("\n--- Total de Vendas: R$ " + String.format("%.2f", totalVendas) + " ---");
		}

		double totalCompras = 0.0;
		System.out.println("\n--- Transações de Saída (Compras) ---");
		if (caixa.getSaida().isEmpty()) {
			System.out.println("  Nenhuma compra registrada.");
		} else {
			for (Compra compra : caixa.getSaida()) {
				String funcInfo = (compra.getFuncionario() != null)
						? "Funcionário: " + compra.getFuncionario().getNome()
						: "Funcionário: N/A";
				System.out.println("  ID: " + compra.getId() + ", Data: " + compra.getData() + ", Valor: R$ "
						+ String.format("%.2f", compra.getValor()) + ", Status: " + compra.getStatus() + ", "
						+ funcInfo);

				if (compra.getProdutos() != null && !compra.getProdutos().isEmpty()) {
					System.out.println("    Itens da Compra:");
					for (Itens item : compra.getProdutos()) {
						System.out.println("      - Produto: " + item.getProduto().getDescricao() + ", Qtd: "
								+ item.getQuantidade() + ", Valor Unit.: R$ "
								+ String.format("%.2f", item.getProduto().getValorCompra()));
					}
				}
				totalCompras += compra.getValor();
			}
			System.out.println("\n--- Total de Compras: R$: " + String.format("%.2f", totalCompras) + " ---");
		}
		System.out.println("--------------------------\n");
	}

	private void carregarTransacoesDoBanco(Caixa caixa) {
		caixa.getEntrada().clear();
		caixa.getSaida().clear();

		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();

			String sqlVendas = "SELECT v.Id AS VendaId, t.Valor, t.Data, t.Funcionario_Id, t.Status, v.Transportadora_Id, t.Id AS TransacaoId "
					+ "FROM venda v JOIN transacoes t ON v.transacao_id = t.Id";

			PreparedStatement stmtVendas = conn.prepareStatement(sqlVendas);
			ResultSet rsVendas = stmtVendas.executeQuery();

			while (rsVendas.next()) {
				String vendaId = String.valueOf(rsVendas.getInt("VendaId"));
				double valorVenda = rsVendas.getDouble("Valor");
				LocalDate dataVenda = rsVendas.getDate("Data").toLocalDate();
				String funcionarioIdStr = String.valueOf(rsVendas.getInt("Funcionario_Id"));
				String statusStr = rsVendas.getString("Status");
				int transportadoraId = rsVendas.getInt("Transportadora_Id");

				Funcionario funcionarioVenda = buscarFuncionarioNoBanco(funcionarioIdStr);
				Transportadora transportadoraVenda = buscarTransportadoraNoBancoPorId(transportadoraId);

				ArrayList<Itens> itensVenda = carregarItensDaVendaDoBanco(conn, vendaId);

				Venda venda = new Venda(itensVenda, dataVenda, funcionarioVenda, Status.valueOf(statusStr), valorVenda,
						transportadoraVenda);
				venda.setId(vendaId);

				caixa.getEntrada().add(venda);
			}

			String sqlCompras = "SELECT c.Id AS CompraId, t.Valor, t.Data, t.Funcionario_Id, t.Status, t.Id AS TransacaoId "
					+ "FROM compra c JOIN transacoes t ON c.transacao_id = t.Id"; 

			PreparedStatement stmtCompras = conn.prepareStatement(sqlCompras);
			ResultSet rsCompras = stmtCompras.executeQuery();

			while (rsCompras.next()) {
				String compraId = String.valueOf(rsCompras.getInt("CompraId"));
				double valorCompra = rsCompras.getDouble("Valor");
				LocalDate dataCompra = rsCompras.getDate("Data").toLocalDate();
				String funcionarioIdStr = String.valueOf(rsCompras.getInt("Funcionario_Id"));
				String statusStr = rsCompras.getString("Status");

				Funcionario funcionarioCompra = buscarFuncionarioNoBanco(funcionarioIdStr);

				ArrayList<Itens> itensCompra = carregarItensDaCompraDoBanco(conn, compraId);

				Compra compra = new Compra(itensCompra, dataCompra, funcionarioCompra, Status.valueOf(statusStr),
						valorCompra);
				compra.setId(compraId);

				caixa.getSaida().add(compra);
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao carregar transações do banco de dados: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
				}
			}
		}
	}

	private ArrayList<Itens> carregarItensDaVendaDoBanco(Connection conn, String vendaId) throws SQLException {
		ArrayList<Itens> itens = new ArrayList<>();
		String sql = "SELECT iv.Quantidade, iv.ValorUnitarioNaVenda, p.Id AS ProdutoId, p.Nome, p.ValorVenda, p.ValorCompra, e.Estoque, e.Minimo "
				+ "FROM itens_venda iv " + "JOIN produto p ON iv.Produto_Id = p.Id "
				+ "JOIN estoque e ON p.Id = e.Produto_Id " + "WHERE iv.Venda_Id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, parseId(vendaId, "Venda para Itens")); // ID da transação
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			String produtoId = String.valueOf(rs.getInt("ProdutoId"));
			String nomeProduto = rs.getString("Nome");
			double valorVendaProduto = rs.getDouble("ValorVenda");
			double valorCompraProduto = rs.getDouble("ValorCompra");
			int estoqueAtualProduto = rs.getInt("Estoque");
			int estoqueMinimoProduto = rs.getInt("Minimo");
			int quantidadeItem = rs.getInt("Quantidade");

			try {
				Produto produto = new Produto(produtoId, nomeProduto, valorVendaProduto, valorCompraProduto);
				Estoque estoqueObj = new Estoque(estoqueAtualProduto, estoqueMinimoProduto, produto);
				produto.setEstoqueProduto(estoqueObj);
				itens.add(new Itens(quantidadeItem, produto));
			} catch (ProdutoException e) {
				System.err.println("Erro ao criar objeto Produto para item da venda: " + e.getMessage());
			}
		}
		return itens;
	}

	private ArrayList<Itens> carregarItensDaCompraDoBanco(Connection conn, String compraId) throws SQLException {

		ArrayList<Itens> itens = new ArrayList<>();
		String sql = "SELECT ic.Quantidade, ic.ValorUnitarioNaCompra, p.Id AS ProdutoId, p.Nome, p.ValorVenda, p.ValorCompra, e.Estoque, e.Minimo "
				+ "FROM itens_compra ic " + "JOIN produto p ON ic.Produto_Id = p.Id "
				+ "JOIN estoque e ON p.Id = e.Produto_Id " + "WHERE ic.Compra_Id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, parseId(compraId, "Compra para Itens")); // ID da transação
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			String produtoId = String.valueOf(rs.getInt("ProdutoId"));
			String nomeProduto = rs.getString("Nome");
			double valorVendaProduto = rs.getDouble("ValorVenda");
			double valorCompraProduto = rs.getDouble("ValorCompra");
			int estoqueAtualProduto = rs.getInt("Estoque");
			int estoqueMinimoProduto = rs.getInt("Minimo");
			int quantidadeItem = rs.getInt("Quantidade");

			try {
				Produto produto = new Produto(produtoId, nomeProduto, valorVendaProduto, valorCompraProduto);
				Estoque estoqueObj = new Estoque(estoqueAtualProduto, estoqueMinimoProduto, produto);
				produto.setEstoqueProduto(estoqueObj);
				itens.add(new Itens(quantidadeItem, produto));
			} catch (ProdutoException e) {
				System.err.println("Erro ao criar objeto Produto para item da compra: " + e.getMessage());
			}
		}
		return itens;
	}

	private static Transportadora buscarTransportadoraNoBancoPorId(int idTransportadoraInt) {
		Transportadora transportadora = null;
		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT Id, Nome, Cnpj, Taxa FROM transportadora WHERE Id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idTransportadoraInt);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int idDoBanco = rs.getInt("Id");
				String nome = rs.getString("Nome");
				String cnpj = rs.getString("Cnpj");
				double taxa = rs.getDouble("Taxa");

				transportadora = new Transportadora(idDoBanco, nome, cnpj, taxa);

				List<Regiao> regioesAtendidas = carregarRegioesTransportadoraInterno(conn, idDoBanco);
				for (Regiao r : regioesAtendidas) {
					transportadora.setRegiao(r);
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao buscar transportadora por ID: " + e.getMessage());
			e.printStackTrace();
		}
		return transportadora;
	}

	private static List<Regiao> carregarRegioesTransportadoraInterno(Connection conn, int transportadoraId)
			throws SQLException {
		List<Regiao> regioes = new ArrayList<>();
		String sql = "SELECT regiao_id FROM transportadora_regiao WHERE transportadora_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, transportadoraId);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int regiaoEnumIndex = rs.getInt("regiao_id") - 1;
			if (regiaoEnumIndex >= 0 && regiaoEnumIndex < Regiao.values().length) {
				regioes.add(Regiao.values()[regiaoEnumIndex]);
			} else {
				System.err.println("Aviso: Valor de Regiao inválido (" + (regiaoEnumIndex + 1)
						+ ") encontrado no DB para transportadora ID " + transportadoraId);
			}
		}
		return regioes;
	}
}