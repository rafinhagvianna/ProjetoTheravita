package Gerenciadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Classes.ConexaoBD;
import Classes.Transportadora;
import Enums.Regiao;
import Interfaces.IntTransportadora;
import Validators.CnpjValidator;

public class GerTransportadora implements IntTransportadora {

	private static final String MENSAGEM_ERRO_SQL = "Erro de SQL: ";
	private static final String MENSAGEM_ENTRADA_INVALIDA_NUMERO = "Entrada inválida! Digite um número.";
	private static final String MENSAGEM_ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente.";

	static Scanner scanner = new Scanner(System.in);

	@Override
	public void menu() {
		System.out.println("Escolha uma das opções: ");
		System.out.println("____________________________________________");
		System.out.println("| 1 - Cadastrar                            |");
		System.out.println("| 2 - Listar                               |");
		System.out.println("| 3 - Atualizar                            |");
		System.out.println("| 4 - Visualizar total                     |");
		System.out.println("| 0 - Sair                                 |");
		System.out.println("--------------------------------------------");
		System.out.println();
	}

	@Override
	public void cadastrarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadorasMemoria) {
	    System.out.println("Digite o nome da transportadora: ");
	    String nome = scanner.nextLine();

	    System.out.println("Digite o CNPJ da transportadora: ");
	    String cnpj = scanner.nextLine();

	    if (!CnpjValidator.isValidFormat(cnpj)) { 
	        System.out.println("CNPJ inválido no formato: " + cnpj);
	        return;
	    }

	    if (cnpjJaCadastrado(cnpj)) { 
	        System.out.println("CNPJ já cadastrado: " + cnpj);
	        return;
	    }

		double taxa = -1;
		do {
			try {
				System.out.print("Digite a taxa da transportadora (% em cima da venda cobrado): ");
				taxa = scanner.nextDouble();
				scanner.nextLine();
				if (taxa < 0) {
					System.out.println("A taxa não pode ser negativa!");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
				taxa = -1;
			} catch (Exception e) {
				scanner.nextLine();
				System.out.println(MENSAGEM_ERRO_INESPERADO + " na entrada da taxa. Tente novamente.");
				taxa = -1;
			}
		} while (taxa < 0);

		List<Regiao> regioesAtendidas = new ArrayList<>();
		String continuarAdicionandoRegiao;
		do {
			Regiao regiaoSelecionada = null;
			int regiaoEscolhida = -1;
			do {
				try {
					System.out.println("Escolha a região:");
					Regiao[] regioes = Regiao.values();
					for (int i = 0; i < regioes.length; i++) {
						System.out.println((i + 1) + " - " + regioes[i]);
					}
					System.out.print("Digite o número da região: ");
					regiaoEscolhida = scanner.nextInt();
					scanner.nextLine();

					if (regiaoEscolhida > 0 && regiaoEscolhida <= regioes.length) {
						regiaoSelecionada = regioes[regiaoEscolhida - 1];
					} else {
						System.out.println("Opção de região inválida! Por favor, escolha um número válido.");
						regiaoSelecionada = null;
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO + " para a região.");
					regiaoSelecionada = null;
				} catch (Exception e) {
					scanner.nextLine();
					System.out.println(MENSAGEM_ERRO_INESPERADO + " na seleção da região. Tente novamente.");
					regiaoSelecionada = null;
				}
			} while (regiaoSelecionada == null);

			if (!regioesAtendidas.contains(regiaoSelecionada)) {
				regioesAtendidas.add(regiaoSelecionada);
				System.out.println("Região '" + regiaoSelecionada + "' adicionada.");
			} else {
				System.out.println("Região '" + regiaoSelecionada + "' já foi adicionada.");
			}


			System.out.print("Deseja adicionar outra região? (s/n): ");
			continuarAdicionandoRegiao = scanner.nextLine();
		} while (continuarAdicionandoRegiao.equalsIgnoreCase("s"));

		if (regioesAtendidas.isEmpty()) {
			System.out.println("Nenhuma região válida foi selecionada. Cadastro de transportadora cancelado.");
			return;
		}

		Connection conexao = null;
		int idGeradoBanco = -1;
		try {
			conexao = ConexaoBD.getConexao();
			conexao.setAutoCommit(false); // Inicia a transação

			String sqlTransportadora = "INSERT INTO transportadora (Nome, CNPJ, Taxa) VALUES(?, ?, ?)";
			try (PreparedStatement stmt = conexao.prepareStatement(sqlTransportadora, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, nome);
				stmt.setString(2, cnpj);
				stmt.setDouble(3, taxa);
				stmt.executeUpdate();

				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						idGeradoBanco = rs.getInt(1);
					} else {
						System.err.println("Erro: Não foi possível obter o ID gerado para a transportadora.");
						conexao.rollback(); // Desfaz a transação
						return;
					}
				}
			}
			Transportadora novaTransportadora = new Transportadora(idGeradoBanco, nome, cnpj, taxa);

			for (Regiao r : regioesAtendidas) {
				novaTransportadora.setRegiao(r);
			}

			String sqlRegiao = "INSERT INTO transportadora_regiao (transportadora_id, regiao_id) VALUES (?, ?)";
			try (PreparedStatement stmtRegiao = conexao.prepareStatement(sqlRegiao)) {
				for (Regiao r : regioesAtendidas) {
					stmtRegiao.setInt(1, idGeradoBanco);
					stmtRegiao.setInt(2, r.ordinal() + 1); // Salva o índice do Enum (+1)
					stmtRegiao.addBatch();
				}
				stmtRegiao.executeBatch();
			}

			conexao.commit(); // Confirma a transação
			System.out.println("Transportadora cadastrada com sucesso!");
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao cadastrar a transportadora no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexao != null)
					conexao.rollback();
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
		} finally {
			try {
				if (conexao != null)
					conexao.close();
			} catch (SQLException closeEx) {
				System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
			}
		}
	}

	@Override
	public void listarTransportadoras(ArrayList<Transportadora> transportadorasMemoria) {  
		try (Connection conexao = ConexaoBD.getConexao();
			 PreparedStatement stmt = conexao.prepareStatement("SELECT t.Id, t.cnpj, t.nome, t.taxa FROM Transportadora t");
			 ResultSet rs = stmt.executeQuery()) {

			if (!rs.isBeforeFirst()) {
				System.out.println("Nenhuma transportadora cadastrada.");
				return;
			}

			System.out.println("Lista de transportadoras cadastradas: ");
			while (rs.next()) {
				int idDoBanco = rs.getInt("Id");
				String cnpj = rs.getString("cnpj");
				String nome = rs.getString("nome");
				double taxa = rs.getDouble("taxa");

				Transportadora transportadora = new Transportadora(idDoBanco, nome, cnpj, taxa);

				List<Regiao> regioesAtendidas = carregarRegioesTransportadora(conexao, idDoBanco);
				for (Regiao r : regioesAtendidas) {
					transportadora.setRegiao(r);
				}

				System.out.println("ID: " + transportadora.getIdDB());
				System.out.println("CNPJ: " + cnpj);
				System.out.println("Nome: " + nome);
				System.out.println("Taxa: " + String.format("%.2f", taxa) + "%");

				if (!transportadora.getRegiao().isEmpty()) {
					System.out.print("Regiões atendidas: ");
					for (int i = 0; i < transportadora.getRegiao().size(); i++) {
						System.out.print(transportadora.getRegiao().get(i).toString());
						if (i < transportadora.getRegiao().size() - 1) {
							System.out.print(", ");
						}
					}
					System.out.println();
				} else {
					System.out.println("Regiões atendidas: Nenhuma");
				}
				System.out.println("-------------------------------");
			}
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao listar transportadoras: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<Regiao> carregarRegioesTransportadora(Connection conexao, int idTransportadora) throws SQLException {
		List<Regiao> regioes = new ArrayList<>();
		String sql = "SELECT regiao_id FROM transportadora_regiao WHERE transportadora_id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idTransportadora);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int indiceEnumRegiao = rs.getInt("regiao_id") - 1;
					if (indiceEnumRegiao >= 0 && indiceEnumRegiao < Regiao.values().length) {
						regioes.add(Regiao.values()[indiceEnumRegiao]);
					} else {
						System.err.println("Aviso: Valor de Regiao inválido (" + (indiceEnumRegiao + 1)
								+ ") encontrado no DB para transportadora ID " + idTransportadora);
					}
				}
			}
		}
		return regioes;
	}

	@Override
	public void atualizarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadorasMemoria) { // Parâmetro mantido, mas não usado
		String idParaAtualizarString;
		int idParaAtualizarNumerico = -1;
		Transportadora transportadoraEncontrada = null;

		do {
			System.out.print("Digite o ID da transportadora que deseja atualizar: ");
			idParaAtualizarString = scanner.nextLine();

			try {
				idParaAtualizarNumerico = Integer.parseInt(idParaAtualizarString);
			} catch (NumberFormatException e) {
				System.err.println("ID inválido. Não é um número inteiro: " + idParaAtualizarString);
				idParaAtualizarNumerico = -1;
			}

			if (idParaAtualizarNumerico == -1) {
				System.out.println("Por favor, digite um número inteiro válido para o ID.");
				continue;
			}

			transportadoraEncontrada = buscarTransportadoraNoBancoPorId(idParaAtualizarNumerico);

			if (transportadoraEncontrada == null) {
				System.out.println("Transportadora com ID " + idParaAtualizarNumerico + " não encontrada no banco de dados.");
			}
		} while (transportadoraEncontrada == null);

		int opcao;
		Connection conexao = null;
		try {
			conexao = ConexaoBD.getConexao();
			conexao.setAutoCommit(false); // Inicia transação

			do {
				System.out.println(
						"Qual dado deseja modificar? \n1 - Nome\n2 - CNPJ\n3 - Taxa\n4 - Adicionar Região\n5 - Remover Região\n0 - Sair");

				try {
					opcao = scanner.nextInt();
					scanner.nextLine(); 

					switch (opcao) {
					case 1:
						System.out.print("Digite o novo nome da transportadora: ");
						String novoNome = scanner.nextLine();
						transportadoraEncontrada.setNome(novoNome); // Atualiza objeto em memória
						atualizarCampoTransportadoraBanco(conexao, transportadoraEncontrada.getIdDB(), "Nome", novoNome);
						System.out.println("Nome atualizado com sucesso!");
						break;

					case 2:
					    System.out.print("Digite o novo CNPJ da transportadora: ");
					    String novoCnpj = scanner.nextLine();

					    if (!CnpjValidator.isValidFormat(novoCnpj)) {
					        System.out.println("Novo CNPJ inválido no formato: " + novoCnpj);
					        break;
					    }

					    if (cnpjJaCadastrado(novoCnpj, transportadoraEncontrada.getIdDB())) {
					        System.out.println("CNPJ já cadastrado por outra transportadora: " + novoCnpj);
					        break; 
					    }
					    transportadoraEncontrada.setCnpj(novoCnpj);
					    atualizarCampoTransportadoraBanco(conexao, transportadoraEncontrada.getIdDB(), "CNPJ", novoCnpj);
					    System.out.println("CNPJ atualizado com sucesso!");
					    break;

					case 3:
						double novaTaxa = -1;
						do {
							try {
								System.out.print("Digite a nova taxa da transportadora (% em cima da venda cobrado): ");
								novaTaxa = scanner.nextDouble();
								scanner.nextLine();
								if (novaTaxa < 0) {
									System.out.println("A taxa não pode ser negativa!");
								}
							} catch (InputMismatchException e) {
								scanner.nextLine();
								System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
								novaTaxa = -1;
							} catch (Exception e) {
								scanner.nextLine();
								System.out.println(MENSAGEM_ERRO_INESPERADO + " na entrada da taxa. Tente novamente.");
								novaTaxa = -1;
							}
						} while (novaTaxa < 0);

						if (novaTaxa >= 0) {
							transportadoraEncontrada.setTaxa(novaTaxa); 
							atualizarCampoTransportadoraBanco(conexao, transportadoraEncontrada.getIdDB(), "Taxa", novaTaxa);
							System.out.println("Taxa atualizada com sucesso!");
						} else {
							System.out.println("Atualização de taxa cancelada.");
						}
						break;

					case 4:
						Regiao regiaoParaAdicionar = null;
						int escolhaRegiaoAdicionar = -1;
						do {
							try {
								System.out.println("Escolha a região a adicionar:");
								Regiao[] regioes = Regiao.values();
								for (int i = 0; i < regioes.length; i++) {
									System.out.println((i + 1) + " - " + regioes[i]);
								}
								System.out.print("Digite o número da região: ");
								escolhaRegiaoAdicionar = scanner.nextInt();
								scanner.nextLine();

								if (escolhaRegiaoAdicionar > 0 && escolhaRegiaoAdicionar <= regioes.length) {
									regiaoParaAdicionar = regioes[escolhaRegiaoAdicionar - 1];
								} else {
									System.out.println("Opção de região inválida! Por favor, escolha um número válido.");
									regiaoParaAdicionar = null;
								}
							} catch (InputMismatchException e) {
								scanner.nextLine();
								System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO + " para a região.");
								regiaoParaAdicionar = null;
							} catch (Exception e) {
								scanner.nextLine();
								System.out.println(MENSAGEM_ERRO_INESPERADO + " na seleção da região. Tente novamente.");
								regiaoParaAdicionar = null;
							}
						} while (regiaoParaAdicionar == null);


						if (regiaoParaAdicionar != null) {
							if (!transportadoraEncontrada.atendeRegiao(regiaoParaAdicionar)) {
								transportadoraEncontrada.setRegiao(regiaoParaAdicionar); 
								adicionarRegiaoTransportadoraBanco(conexao, transportadoraEncontrada.getIdDB(),
										regiaoParaAdicionar);
								System.out.println("Região '" + regiaoParaAdicionar + "' adicionada com sucesso!");
							} else {
								System.out.println("Transportadora já atende a região '" + regiaoParaAdicionar + "'.");
							}
						}
						break;

					case 5:
						if (transportadoraEncontrada.getRegiao().isEmpty()) {
							System.out.println("Esta transportadora não possui regiões cadastradas para remover.");
							break;
						}
						System.out.println("Regiões atualmente atendidas:");
						for (int i = 0; i < transportadoraEncontrada.getRegiao().size(); i++) {
							System.out.println((i + 1) + " - " + transportadoraEncontrada.getRegiao().get(i));
						}

						Regiao regiaoParaRemover = null;
						try {
							System.out.print("Digite o número da região a ser removida: ");
							int escolhaRegiao = scanner.nextInt();
							scanner.nextLine();
							if (escolhaRegiao > 0 && escolhaRegiao <= transportadoraEncontrada.getRegiao().size()) {
								regiaoParaRemover = transportadoraEncontrada.getRegiao().get(escolhaRegiao - 1);
								transportadoraEncontrada.removerRegiao(regiaoParaRemover); // Remove em memória
								removerRegiaoTransportadoraBanco(conexao, transportadoraEncontrada.getIdDB(),
										regiaoParaRemover);
								System.out.println("Região '" + regiaoParaRemover + "' removida com sucesso!");
							} else {
								System.out.println("Opção inválida!");
							}
						} catch (InputMismatchException e) {
							scanner.nextLine();
							System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
						}
						break;

					case 0:
						System.out.println("Saindo da atualização...");
						break;

					default:
						System.out.println("Opção inválida, tente novamente.");
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println(MENSAGEM_ENTRADA_INVALIDA_NUMERO);
					opcao = -1; // Para continuar o loop
				} catch (Exception e) {
					scanner.nextLine();
					System.out.println(MENSAGEM_ERRO_INESPERADO + e.getMessage());
					e.printStackTrace();
					opcao = -1;
				}
			} while (opcao != 0);

			conexao.commit(); 
			System.out.println("Dados da transportadora atualizados com sucesso no banco de dados!");

		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao atualizar transportadora no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexao != null)
					conexao.rollback(); // Tenta desfazer em caso de erro
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
		} finally {
			try {
				if (conexao != null)
					conexao.close();
			} catch (SQLException closeEx) {
				System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
			}
		}
	}

	private void atualizarCampoTransportadoraBanco(Connection conexao, int idTransportadoraBanco, String campo,
			Object novoValor) throws SQLException {
		String sql = "UPDATE transportadora SET " + campo + " = ? WHERE Id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			if (novoValor instanceof String) {
				stmt.setString(1, (String) novoValor);
			} else if (novoValor instanceof Double) {
				stmt.setDouble(1, (Double) novoValor);
			} else if (novoValor instanceof Integer) { // Adicionado para cobrir inteiros, se houver campos int
				stmt.setInt(1, (Integer) novoValor);
			} else {
				System.err.println("Tipo de valor inesperado para atualização do campo '" + campo + "'.");
				return;
			}
			stmt.setInt(2, idTransportadoraBanco);
			stmt.executeUpdate();
		}
	}

	private void adicionarRegiaoTransportadoraBanco(Connection conexao, int idTransportadoraBanco, Regiao regiao)
			throws SQLException {
		String sql = "INSERT INTO transportadora_regiao (transportadora_id, regiao_id) VALUES (?, ?)";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idTransportadoraBanco);
			stmt.setInt(2, regiao.ordinal() + 1); // Salva o índice do Enum (+1)
			stmt.executeUpdate();
		}
	}

	private void removerRegiaoTransportadoraBanco(Connection conexao, int idTransportadoraBanco, Regiao regiao)
			throws SQLException {
		String sql = "DELETE FROM transportadora_regiao WHERE transportadora_id = ? AND regiao_id = ?";
		try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
			stmt.setInt(1, idTransportadoraBanco);
			stmt.setInt(2, regiao.ordinal() + 1);
			stmt.executeUpdate();
		}
	}

	private Transportadora buscarTransportadoraNoBancoPorId(int idBanco) {
		Transportadora transportadora = null;
		Connection conexao = null;
		try {
			conexao = ConexaoBD.getConexao();
			String sql = "SELECT Id, Nome, CNPJ, Taxa FROM transportadora WHERE Id = ?";
			try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
				stmt.setInt(1, idBanco);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {

						transportadora = new Transportadora(rs.getInt("Id"), rs.getString("Nome"),
								rs.getString("CNPJ"), rs.getDouble("Taxa"));
						List<Regiao> regioesAtendidas = carregarRegioesTransportadora(conexao, transportadora.getIdDB());
						for (Regiao r : regioesAtendidas) {
							transportadora.setRegiao(r);
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao buscar transportadora por ID (numérico): " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (conexao != null)
					conexao.close();
			} catch (SQLException closeEx) {
				System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
			}
		}
		return transportadora;
	}

	@Override
	public void visualizarTotalTransportadoras(ArrayList<Transportadora> transportadorasMemoria) { 
		int totalTransportadoras = getTotalTransportadorasDoBanco();
		System.out.println("Total de transportadoras cadastradas no banco de dados: " + totalTransportadoras);
	}

	private int getTotalTransportadorasDoBanco() {
		int total = 0;
		try (Connection conexao = ConexaoBD.getConexao();
			 PreparedStatement stmt = conexao.prepareStatement("SELECT COUNT(*) FROM Transportadora");
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println(MENSAGEM_ERRO_SQL + "ao recuperar o total de transportadoras: " + e.getMessage());
			e.printStackTrace();
		}
		return total;
	}

    private boolean cnpjJaCadastrado(String cnpj) {
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement("SELECT COUNT(*) FROM Transportadora WHERE CNPJ = ?")) {
            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println(MENSAGEM_ERRO_SQL + "ao verificar CNPJ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

	private boolean cnpjJaCadastrado(String cnpj, int idTransportadoraAtual) {
        try (Connection conexao = ConexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement("SELECT COUNT(*) FROM Transportadora WHERE CNPJ = ? AND Id != ?")) {
            stmt.setString(1, cnpj);
			stmt.setInt(2, idTransportadoraAtual);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println(MENSAGEM_ERRO_SQL + "ao verificar CNPJ (atualização): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}