package Gerenciadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import Classes.*;
import Exceptions.FuncionarioException;
import Interfaces.IntFuncionario;
import Validators.CpfValidator;
import Validators.FuncionarioValidator;
import Enums.Setores;

public class GerFuncionario implements IntFuncionario {

	private static final String MSG_ENTRADA_INVALIDA_NUMERO = "Entrada inválida! Digite um número.";
	private static final String MSG_ERRO_INESPERADO = "Ocorreu um erro inesperado. Tente novamente.";
	private static final String MSG_ERRO_SQL = "Erro de SQL: ";

	public void menu() {
		System.out.println("Escolha uma das opções: ");
		System.out.println("-----------------------------------------");
		System.out.println("| 1 - Cadastrar funcionário             |");
		System.out.println("| 2 - Listar funcionários (por setor)   |");
		System.out.println("| 3 - Editar funcionário                |");
		System.out.println("| 4 - Excluir funcionário               |");
		System.out.println("| 5 - Visualizar total de funcionários  |");
		System.out.println("| 0 - Sair                              |");
		System.out.println("-----------------------------------------");
		System.out.println();
	}

	public void cadastrarFuncionario(Scanner scanner, ArrayList<Setor> setores, ArrayList<Funcionario> funcionarios) {
		System.out.print("Nome = ");
		String nome = scanner.nextLine();
		
		if (!FuncionarioValidator.isValidNome(nome)) {
            return; 
        }
		
		System.out.print("CPF = ");
		String cpf = scanner.next();
		scanner.nextLine();
		
		if (!CpfValidator.isValid(cpf)) {
			System.out.println("Erro: CPF inválido! O CPF deve conter exatamente 11 dígitos.");
            return; 
		}
		
		if (CpfValidator.cpfJaExisteNoBanco(cpf)) {
	        System.out.println("Erro: Já existe um funcionário cadastrado com este CPF. Cadastro cancelado.");
	        return; 
	    }
		System.out.print("Gênero (H) Homem, (M) Mulher ou (O) Outros = ");
		String genero = scanner.next();
		scanner.nextLine();
		genero = genero.toUpperCase();
		
		Setor setorSelecionado = solicitarSetor(scanner, setores);
		if (setorSelecionado == null) {
			System.out.println("Operação de cadastro cancelada devido a seleção de setor inválida.");
			return;
		}

		double salarioBase = solicitarSalarioBase(scanner);
		if (salarioBase == -1.0) {
			System.out.println("Operação de cadastro cancelada devido a entrada de salário inválida.");
			return;
		}

		Funcionario funcionario = null;
		Salario salarioFuncionario = null;
		try {
			funcionario = new Funcionario(null, nome, cpf, genero, setorSelecionado, null);
			salarioFuncionario = new Salario(salarioBase, funcionario);
			funcionario.setSalario(salarioFuncionario);
		} catch (FuncionarioException e) {
			System.err.println("Erro ao criar funcionário ou salário em memória: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();
			conn.setAutoCommit(false); // Inicia a transação

			
			String sqlFuncionario = "INSERT INTO funcionario (Nome, CPF, Genero, Setor_Id, Salario) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario, Statement.RETURN_GENERATED_KEYS)) {
				stmtFuncionario.setString(1, nome);
				stmtFuncionario.setString(2, cpf);
				stmtFuncionario.setString(3, genero);
				stmtFuncionario.setInt(4, setorSelecionado.getId());
				stmtFuncionario.setDouble(5, salarioBase);
				
				stmtFuncionario.executeUpdate();

				try (ResultSet generatedKeys = stmtFuncionario.getGeneratedKeys()) {
					int funcionarioIdNoBanco = -1;
					if (generatedKeys.next()) {
						funcionarioIdNoBanco = generatedKeys.getInt(1);
						funcionario.setId(String.valueOf(funcionarioIdNoBanco));
					} else {
						System.err.println("Erro: Não foi possível obter o ID gerado para o funcionário.");
						conn.rollback();
						return;
					}
				}
			}

			String sqlSalario = "INSERT INTO Salario (Salario, Funcionario_Id, NomeFuncionario) VALUES (?, ?, ?)";
			try (PreparedStatement stmtSalario = conn.prepareStatement(sqlSalario)) {
				stmtSalario.setDouble(1, salarioBase);
				stmtSalario.setInt(2, funcionario.getId() != null ? Integer.parseInt(funcionario.getId()) : -1);
				stmtSalario.setString(3, nome);
				stmtSalario.executeUpdate();
			}

			conn.commit();
			System.out.println("Funcionário e salário cadastrados com sucesso no banco de dados!");

		} catch (SQLException e) {
			System.err.println("Erro ao cadastrar funcionário e salário no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null) {
				    System.err.println("Tentando realizar o rollback.");
				    conn.rollback();
				    System.err.println("Rollback concluído.");
				}
			} catch (SQLException rollbackEx) {
				System.err.println("Erro ao tentar fazer rollback: " + rollbackEx.getMessage());
			}
		} finally {
		    try {
		        if (conn != null) {
		            conn.close();
		        }
		    } catch (SQLException closeEx) {
		        System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
		    }
		}

		setorSelecionado.getFuncionarios().add(funcionario);
		funcionarios.add(funcionario);
	}

	public void listarFuncionariosPorSetor(Scanner scanner, ArrayList<Setor> setores) {
		Setor setorSelecionado = solicitarSetor(scanner, setores);
		if (setorSelecionado == null) {
			System.out.println("Listagem cancelada devido a seleção de setor inválida.");
			return;
		}

		System.out.println("Funcionários do setor " + setorSelecionado.getNome() + ":");

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.cpf, f.nome, f.genero, f.Setor_Id, f.Salario AS SalarioBase, s.nome as setor_nome "
					+ "FROM funcionario f JOIN setor s ON f.Setor_Id = s.id " + "WHERE s.id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, setorSelecionado.getId());
				try (ResultSet rs = stmt.executeQuery()) {

					boolean temFuncionarios = false;
					while (rs.next()) {
						Funcionario funcionario = criarFuncionarioDoResultSet(rs, setorSelecionado);
						if (funcionario != null) {
							System.out.println(funcionario.toString());
							temFuncionarios = true;
						}
					}

					if (!temFuncionarios) {
						System.out.println("Não há funcionários cadastrados nesse setor.");
					}
				}
			}

		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao carregar os funcionários do banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void editarFuncionario(Scanner scanner, ArrayList<Setor> setores) {
		String idFuncionarioEditar;
		Funcionario funcionarioEditar = null;

		do {
			System.out.print("Informe o ID do funcionário que deseja editar: ");
			idFuncionarioEditar = scanner.next();
			scanner.nextLine();

			funcionarioEditar = buscarFuncionarioPorIdNoBanco(idFuncionarioEditar, setores);

			if (funcionarioEditar == null) {
				System.out.println(
						"Funcionário com ID " + idFuncionarioEditar + " não encontrado no banco de dados.");
			}
		} while (funcionarioEditar == null);

		int opc;
		Connection conn = null; 
		try {
			conn = ConexaoBD.getConexao();
			conn.setAutoCommit(false); // Inicia transação

			do {
				System.out.println(
						"Qual dado deseja modificar? \n1 - Nome\n2 - CPF\n3 - Gênero\n4 - Setor\n5 - Salário\n0 - Sair");

				try {
					opc = scanner.nextInt();
					scanner.nextLine();
					switch (opc) {
					case 1:
						System.out.print("Insira o novo nome = ");
						String novoNome = scanner.nextLine();
						try {
							funcionarioEditar.setNome(novoNome);
							atualizarCampoFuncionarioBanco(conn, funcionarioEditar.getId(), "Nome", novoNome); 
						} catch (FuncionarioException e) {
							System.err.println("Erro ao definir nome no objeto: " + e.getMessage());
						}
						break;
					case 2:
						System.out.print("Insira o novo CPF = ");
						String novoCpf = scanner.nextLine();
						try {
							funcionarioEditar.setCpf(novoCpf);
							atualizarCampoFuncionarioBanco(conn, funcionarioEditar.getId(), "CPF", novoCpf); 
						} catch (FuncionarioException funEx) {
							System.err.println("Erro ao definir CPF no objeto: " + funEx.getMessage());
						}
						break;
					case 3:
						System.out.print("Insira o novo gênero = ");
						String novoGenero = scanner.nextLine();
						try {
							funcionarioEditar.setGenero(novoGenero);
							atualizarCampoFuncionarioBanco(conn, funcionarioEditar.getId(), "Genero", novoGenero);
						} catch (FuncionarioException e) {
							System.err.println("Erro ao definir gênero no objeto: " + e.getMessage());
						}
						break;
					case 4:
						Setor novoSetor = solicitarSetor(scanner, setores);
						if (novoSetor != null) {
							funcionarioEditar.setSetor(novoSetor);
							atualizarCampoFuncionarioBanco(conn, funcionarioEditar.getId(), "Setor_Id", novoSetor.getId()); 
							System.out.println("Setor atualizado com sucesso!");
						} else {
							System.out.println("Atualização de setor cancelada.");
						}
						break;
					case 5:
						double novoSalario = solicitarSalarioBase(scanner);
						if (novoSalario != -1.0) {
							try {
								funcionarioEditar.getSalario().setSalario(novoSalario);
								atualizarCampoFuncionarioBanco(conn, funcionarioEditar.getId(), "Salario", novoSalario); 
								atualizarSalarioTabelaSalario(conn, funcionarioEditar.getId(), novoSalario); 
								System.out.println("Salário atualizado com sucesso!");
							} catch (FuncionarioException e) {
								System.err.println("Erro ao definir salário no objeto: " + e.getMessage());
							}
						} else {
							System.out.println("Atualização de salário cancelada.");
						}
						break;
					case 0:
						break;
					default:
						System.out.println("Entrada inválida! Digite um número entre 0 e 5.");
						opc = -1;
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println(MSG_ENTRADA_INVALIDA_NUMERO);
					opc = -1;
				}
			} while (opc != 0);

			conn.commit(); // Confirma todas as operações da sessão de atualização
			System.out.println("Dados do funcionário atualizados com sucesso no banco de dados!");

		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao atualizar funcionário no banco de dados: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback(); // Tenta desfazer em caso de erro
			} catch (SQLException rbEx) {
				System.err.println("Erro ao tentar rollback: " + rbEx.getMessage());
			}
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException closeEx) {
				System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
			}
		}
	}
	private void atualizarCampoFuncionarioBanco(Connection conn, String idFuncionarioString, String campo, Object novoValor) {
		int idFuncionarioInt = parseIdFuncionario(idFuncionarioString);
		if (idFuncionarioInt == -1)
			return;

		try (PreparedStatement stmt = conn.prepareStatement("UPDATE funcionario SET " + campo + " = ? WHERE Id = ?")) {
			if (novoValor instanceof String) {
				stmt.setString(1, (String) novoValor);
			} else if (novoValor instanceof Double) {
				stmt.setDouble(1, (Double) novoValor);
			} else if (novoValor instanceof Integer) {
				stmt.setInt(1, (Integer) novoValor);
			} else {
				System.err.println("Tipo de valor inesperado para atualização do campo: " + campo);
				return;
			}

			stmt.setInt(2, idFuncionarioInt);
			stmt.executeUpdate();
			System.out.println("Campo '" + campo + "' do funcionário atualizado no banco de dados.");
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao atualizar campo '" + campo + "' do funcionário: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void atualizarSalarioTabelaSalario(Connection conn, String funcionarioIdString, double novoSalarioBase) {
		int funcionarioIdInt = parseIdFuncionario(funcionarioIdString);
		if (funcionarioIdInt == -1)
			return;

		try (PreparedStatement stmt = conn.prepareStatement("UPDATE Salario SET Salario = ? WHERE Funcionario_Id = ?")) {
			stmt.setDouble(1, novoSalarioBase);
			stmt.setInt(2, funcionarioIdInt);
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				System.out.println("Aviso: Não foi encontrada entrada na tabela 'Salario' para o funcionário ID "
						+ funcionarioIdInt + " para atualização. Considere inseri-la primeiro.");
			} else {
				System.out.println("Salário e benefícios do funcionário atualizados na tabela Salario.");
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao atualizar salário na tabela Salario: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void carregarFuncionariosDoBanco(ArrayList<Funcionario> funcionarios, ArrayList<Setor> setores) {
		funcionarios.clear(); 
		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.cpf, f.nome, f.genero, f.Salario AS SalarioBase, s.id as setor_id, s.nome as setor_nome "
					+ "FROM funcionario f JOIN setor s ON f.Setor_Id = s.id";
			try (PreparedStatement stmt = conn.prepareStatement(sql);
				 ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					int setorId = rs.getInt("setor_id");
					Setor setorDoFuncionario = setores.stream().filter(s -> s.getId() == setorId).findFirst().orElse(null);

					if (setorDoFuncionario == null) {
						System.err.println("Aviso: Setor ID " + setorId + " para funcionário " + rs.getString("nome")
								+ " não encontrado na lista de setores em memória. Pulando este funcionário.");
						continue;
					}

					Funcionario funcionario = criarFuncionarioDoResultSet(rs, setorDoFuncionario);
					if (funcionario != null) {
						funcionarios.add(funcionario);
						setorDoFuncionario.adicionarFuncionario(funcionario); 
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao carregar os funcionários do banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private Salario carregarSalarioDoBanco(String idFuncionarioString, Funcionario funcionario,
			double salarioBaseFuncionario) {
		int idFuncionarioInt = parseIdFuncionario(idFuncionarioString);
		if (idFuncionarioInt == -1)
			return new Salario(); 

		Salario salario = null;
		try (Connection conn = ConexaoBD.getConexao()) {

			String sql = "SELECT Salario FROM Salario WHERE Funcionario_Id = ?"; 
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, idFuncionarioInt);
				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {

						try {
							salario = new Salario(salarioBaseFuncionario, funcionario);

						} catch (FuncionarioException e) {
							System.err.println("Erro ao criar objeto Salario para funcionário " + idFuncionarioString + ": "
									+ e.getMessage());
							e.printStackTrace();
							salario = new Salario();
						}
					} else {
						System.out.println(
								"Aviso: Entrada de salário/benefícios não encontrada na tabela 'Salario' para o funcionário ID "
										+ idFuncionarioInt + ". Usando salário base padrão.");
						try {
							salario = new Salario(salarioBaseFuncionario, funcionario);
						} catch (FuncionarioException e) {
							System.err.println("Erro ao criar objeto Salario padrão para funcionário " + idFuncionarioString
									+ ": " + e.getMessage());
							salario = new Salario();
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao carregar salário/benefícios para funcionário " + idFuncionarioString
					+ ": " + e.getMessage());
			e.printStackTrace();
			return new Salario();
		}
		return salario;
	}

	public void excluirFuncionario(Scanner scanner, ArrayList<Funcionario> funcionarios) {
		String cpfExcluir = scanner.next();
		scanner.nextLine();

		Funcionario funcRemover = buscarFuncionarioPorCpf(cpfExcluir);

		if (funcRemover != null) {
			
			funcionarios.removeIf(f -> f.getCpf().equals(cpfExcluir));
			if (funcRemover.getSetor() != null) {
				funcRemover.getSetor().removerFuncionario(funcRemover);
			}

			Connection conn = null; 
			try {
				conn = ConexaoBD.getConexao();
				conn.setAutoCommit(false); // Inicia a transação para exclusão

				String deleteSalarioSql = "DELETE FROM Salario WHERE Funcionario_Id = ?";
				try (PreparedStatement stmtSalario = conn.prepareStatement(deleteSalarioSql)) {
					stmtSalario.setInt(1, Integer.parseInt(funcRemover.getId()));
					stmtSalario.executeUpdate();
				}

				String deleteFuncionarioSql = "DELETE FROM funcionario WHERE Id = ?";
				try (PreparedStatement stmtFuncionario = conn.prepareStatement(deleteFuncionarioSql)) {
					stmtFuncionario.setInt(1, Integer.parseInt(funcRemover.getId()));
					int rowsAffected = stmtFuncionario.executeUpdate();

					if (rowsAffected > 0) {
						conn.commit(); // Confirma a transação
						System.out.println("Funcionário e seus dados de salário removidos com sucesso do banco de dados!");
					} else {
						conn.rollback(); // Desfaz se nada foi afetado
						System.out.println(
								"Funcionário com CPF " + cpfExcluir + " não encontrado no banco de dados para exclusão.");
					}
				}

			} catch (SQLException e) {
				System.err.println(MSG_ERRO_SQL + "ao remover o funcionário do banco de dados: " + e.getMessage());
				e.printStackTrace();
				try {
					if (conn != null) { 
				        System.err.println("Tentando rollback...");
				        conn.rollback();
				        System.err.println("Rollback concluído.");
					}
				} catch (SQLException rollbackEx) {
					System.err.println("Erro ao tentar fazer rollback: " + rollbackEx.getMessage());
				}
			} catch (NumberFormatException e) {
				System.err.println("Erro de formato de ID ao excluir funcionário: " + e.getMessage());
			} finally {
				try {
					if (conn != null) {
						conn.close(); 
					}
				} catch (SQLException closeEx) {
					System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
				}
			}
		} else {
			System.out.println("Funcionário com CPF " + cpfExcluir + " não encontrado.");
		}
	}

	private Setor solicitarSetor(Scanner scanner, ArrayList<Setor> setores) {
		Setor setorSelecionado = null;
		do {
			try {
				System.out.println("Escolha o setor:");
				for (int i = 0; i < setores.size(); i++) {
					System.out.println((i + 1) + " - " + setores.get(i).getNome());
				}
				int idSetorEscolhidoPeloUsuario = scanner.nextInt();
				scanner.nextLine(); // Consumir a nova linha

				if (idSetorEscolhidoPeloUsuario > 0 && idSetorEscolhidoPeloUsuario <= setores.size()) {
					setorSelecionado = setores.get(idSetorEscolhidoPeloUsuario - 1);
				} else {
					System.out.println("Opção de setor inválida! Por favor, escolha um número válido.");
					setorSelecionado = null; // Garante que o loop continue
				}
			} catch (InputMismatchException e) {
				scanner.nextLine(); // Limpa o buffer do scanner
				System.out.println(MSG_ENTRADA_INVALIDA_NUMERO + " para o setor.");
				setorSelecionado = null;
			} catch (Exception e) {
				scanner.nextLine(); // Limpa o buffer do scanner
				System.out.println(MSG_ERRO_INESPERADO + " na seleção do setor. Tente novamente.");
				setorSelecionado = null;
			}
		} while (setorSelecionado == null);
		return setorSelecionado;
	}

	private double solicitarSalarioBase(Scanner scanner) {
		double salarioBase = -1;
		do {
			try {
				System.out.print("Salário base = ");
				salarioBase = scanner.nextDouble();
				scanner.nextLine(); 
				if (salarioBase < 0) {
					System.out.println("O salário base não pode ser negativo!");
				}
			} catch (InputMismatchException e) {
				scanner.nextLine(); 
				System.out.println("Tipo inserido inválido. Digite um valor real para o salário!");
				salarioBase = -1; 
			} catch (Exception e) {
				scanner.nextLine(); 
				System.out.println(MSG_ERRO_INESPERADO + " na entrada do salário. Tente novamente.");
				salarioBase = -1;
			}
		} while (salarioBase < 0);
		return salarioBase;
	}

	private Funcionario buscarFuncionarioPorIdNoBanco(String idFuncionarioString, ArrayList<Setor> setores) {
		int idFuncionarioInt = parseIdFuncionario(idFuncionarioString);
		if (idFuncionarioInt == -1) {
			return null;
		}

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, "
					+ "s.id AS SetorId, s.nome AS SetorNome "
					+ "FROM funcionario f JOIN setor s ON f.Setor_Id = s.id WHERE f.id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, idFuncionarioInt);
				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {

						int setorId = rs.getInt("SetorId");
						Setor setorDoFuncionario = setores.stream().filter(s -> s.getId() == setorId).findFirst().orElse(null);

						if (setorDoFuncionario == null) {
							System.err.println("Aviso: Setor ID " + setorId + " para funcionário " + rs.getString("Nome")
									+ " não encontrado na lista de setores em memória. Não será possível criar o objeto Funcionario completo.");
							return null;
						}
						return criarFuncionarioDoResultSet(rs, setorDoFuncionario);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao buscar funcionário por ID: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private Funcionario buscarFuncionarioPorCpf(String cpf) {
		Funcionario funcionario = null;
		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, "
					+ "s.id as SetorId, s.nome as SetorNome FROM funcionario f "
					+ "JOIN setor s ON f.Setor_Id = s.id WHERE f.CPF = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, cpf);
				try (ResultSet rs = stmt.executeQuery()) {

					if (rs.next()) {
						String setorNomeStr = rs.getString("SetorNome");
						Setor setor = null;
						try {
							setor = new Setor(Setores.valueOf(setorNomeStr.toUpperCase()));
						} catch (IllegalArgumentException e) {
							System.err.println("Aviso: Setor '" + setorNomeStr + "' não mapeado em Enum Setores. Funcionario: "
									+ rs.getString("Nome"));
							return null;
						}
						funcionario = criarFuncionarioDoResultSet(rs, setor);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println(MSG_ERRO_SQL + "ao buscar funcionário por CPF: " + e.getMessage());
			e.printStackTrace();
		}
		return funcionario;
	}

	private Funcionario criarFuncionarioDoResultSet(ResultSet rs, Setor setor) {
		try {
			String idDoBanco = String.valueOf(rs.getInt("id"));
			String cpf = rs.getString("cpf");
			String nome = rs.getString("nome");
			String genero = rs.getString("genero");
			double salarioBase = rs.getDouble("SalarioBase"); 

			Funcionario funcionario = new Funcionario(idDoBanco, nome, cpf, genero, setor, null);
			Salario salario = carregarSalarioDoBanco(idDoBanco, funcionario, salarioBase);
			funcionario.setSalario(salario);
			return funcionario;
		} catch (SQLException | FuncionarioException e) {
			System.err.println("Erro ao criar objeto Funcionário a partir do ResultSet: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private int parseIdFuncionario(String idString) {
		try {
			return Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			System.err.println("Erro: ID do funcionário inválido. Não é um número inteiro: " + idString);
			return -1;
		}
	}

	public static int getTotalFuncionarios() {
		int total = 0;
		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT COUNT(*) AS total FROM funcionario";
			try (PreparedStatement stmt = conn.prepareStatement(sql);
				 ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					total = rs.getInt("total");
				}
			}
		} catch (SQLException e) {
			System.err.println("Erro ao obter o total de funcionários do banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
		return total;
	}
}