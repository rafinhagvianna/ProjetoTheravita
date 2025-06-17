package Gerenciadores;

import Interfaces.IntSalario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Classes.*;
import Enums.Setores;
import Enums.Status;
import Exceptions.FuncionarioException;

public class GerSalario implements IntSalario {
	Scanner scanner = new Scanner(System.in);

	@Override
	public void menu() {
		System.out.println("Escolha uma das opções: ");
		System.out.println("-------------------------------------------");
		System.out.println("| 1 - Apresentar salário bruto            |");
		System.out.println("| 2 - Calcular salário líquido            |");
		System.out.println("| 3 - Consultar valores dos benefícios    |");
		System.out.println("| 4 - Exibir demonstrativo salarial       |");
		System.out.println("| 5 - Valor da bonificação p/ funcionário |");
		System.out.println("| 0 - Sair                                |");
		System.out.println("-------------------------------------------");
		System.out.println();
	}

	@Override
	public void salarioBruto(ArrayList<Funcionario> funcionarios) {
		System.out.print("Informe o ID do funcionário: ");
		int idFuncionario = -1;
		try {
			idFuncionario = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("ID inválido. Por favor, digite um número inteiro.");
			scanner.nextLine();
			return;
		}

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT id, Nome, Salario FROM funcionario WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idFuncionario);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String nomeDoBanco = rs.getString("Nome");
				double salarioBrutoDoBanco = rs.getDouble("Salario");

				System.out.println("Nome do Funcionário: " + nomeDoBanco);
				System.out.println("ID do Funcionário: " + id);
				System.out.println("Salário Bruto: R$ " + String.format("%.2f", salarioBrutoDoBanco));
			} else {
				System.out.println("Funcionário com ID " + idFuncionario + " não encontrado no banco de dados.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar salário bruto do funcionário no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void calcularSalario(ArrayList<Funcionario> funcionarios) {
		System.out.print("Informe o ID do funcionário: ");
		int idFuncionario = -1;
		try {
			idFuncionario = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("ID inválido. Por favor, digite um número inteiro.");
			scanner.nextLine();
			return;
		}

		Funcionario funcionarioCompleto = getFuncionarioCompletoDoBanco(idFuncionario);

		if (funcionarioCompleto != null) {
			System.out.println("Nome do Funcionário: " + funcionarioCompleto.getNome());
			System.out.println("ID do Funcionário: " + idFuncionario);
			System.out.println(
					"Salário Bruto: R$ " + String.format("%.2f", funcionarioCompleto.getSalario().getSalario()));
			System.out.println(
					"Salário Líquido: R$ " + String.format("%.2f", funcionarioCompleto.getSalario().calculaSalario()));
		} else {
			System.out.println("Funcionário com ID " + idFuncionario + " não encontrado no banco de dados.");
		}
	}

	private Funcionario getFuncionarioCompletoDoBanco(int id) {
		Funcionario funcionario = null;
		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, "
					+ "s.id AS SetorId, s.nome AS SetorNome " + "FROM funcionario f "
					+ "JOIN setor s ON f.Setor_Id = s.id " + "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String nome = rs.getString("Nome");
				String cpf = rs.getString("CPF");
				String genero = rs.getString("Genero");
				double salarioBase = rs.getDouble("SalarioBase");
				int setorId = rs.getInt("SetorId");
				String setorNomeStr = rs.getString("SetorNome");

				Setores setorEnum = null;
				try {
					setorEnum = Setores.valueOf(setorNomeStr.toUpperCase());
				} catch (IllegalArgumentException e) {
					System.err.println("Erro: Nome do setor '" + setorNomeStr
							+ "' não corresponde a nenhum enum Setores. Funcionário ID " + id + ".");
					return null;
				}

				Setor setor = new Setor(setorEnum);

				try {

					funcionario = new Funcionario(nome, cpf, genero, setor, null);

					Salario salarioObj = new Salario(salarioBase, funcionario);
					funcionario.setSalario(salarioObj);

				} catch (FuncionarioException e) {
					System.out
							.println("Erro ao criar objeto Funcionário a partir dos dados do banco: " + e.getMessage());
					e.printStackTrace();
					funcionario = null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar funcionário completo no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
		return funcionario;
	}
	
	@Override
	public void consultarBeneficios(ArrayList<Funcionario> funcionarios) {
		System.out.print("Informe o ID do funcionário: ");
		int idFuncionario = -1;
		try {
			idFuncionario = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("ID inválido. Por favor, digite um número inteiro.");
			scanner.nextLine();
			return;
		}

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, " + "s.nome AS SetorNome "
					+ "FROM funcionario f " + "JOIN setor s ON f.Setor_Id = s.id " + "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idFuncionario);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("Nome");
				String cpf = rs.getString("CPF");
				String genero = rs.getString("Genero");
				double salarioBase = rs.getDouble("SalarioBase");
				String setorNomeStr = rs.getString("SetorNome");

				Funcionario funcionarioParaBeneficios = null;
				try {
					Setores setorEnum = Setores.valueOf(setorNomeStr.toUpperCase());
					Setor setor = new Setor(setorEnum);

					funcionarioParaBeneficios = new Funcionario(nome, cpf, genero, setor, null);
					Salario salarioObj = new Salario(salarioBase, funcionarioParaBeneficios);
					funcionarioParaBeneficios.setSalario(salarioObj);

				} catch (FuncionarioException e) {
					System.out.println("Erro ao criar objeto Funcionário em memória: " + e.getMessage());
					e.printStackTrace();
					return;
				} catch (IllegalArgumentException e) {
					System.out.println(
							"Erro: Nome do setor '" + setorNomeStr + "' não corresponde a nenhum enum Setores.");
					return;
				}

				Salario salarioFuncionario = funcionarioParaBeneficios.getSalario();
				System.out.println("Benefícios de " + nome + ":");
				System.out.println("ID do Funcionário: " + id);
				System.out.println(" - Plano de saúde: R$ " + String.format("%.2f", salarioFuncionario.getSaude()));
				System.out.println(" - Vale refeição/alimentação: R$ "
						+ String.format("%.2f", salarioFuncionario.getValeRefAliment()));
				System.out
						.println(" - Plano odontológico: R$ " + String.format("%.2f", salarioFuncionario.getOdonto()));
			} else {
				System.out.println("Funcionário com ID " + idFuncionario + " não encontrado.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar benefícios do funcionário no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public void exibirDemonstrativo(ArrayList<Funcionario> funcionarios) {
		System.out.print("Informe o ID do funcionário: ");
		int idFuncionario = -1;
		try {
			idFuncionario = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("ID inválido. Por favor, digite um número inteiro.");
			scanner.nextLine();
			return;
		}

		try (Connection conn = ConexaoBD.getConexao()) {
			String sql = "SELECT f.id, f.Nome, f.CPF, f.Genero, f.Salario AS SalarioBase, " + "s.nome AS SetorNome "
					+ "FROM funcionario f " + "JOIN setor s ON f.Setor_Id = s.id " + "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idFuncionario);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("Nome");
				String cpf = rs.getString("CPF");
				String genero = rs.getString("Genero");
				double salarioBase = rs.getDouble("SalarioBase");
				String setorNomeStr = rs.getString("SetorNome");

				Funcionario funcionarioParaDemonstrativo = null;
				try {
					Setores setorEnum = Setores.valueOf(setorNomeStr.toUpperCase());
					Setor setor = new Setor(setorEnum);

					funcionarioParaDemonstrativo = new Funcionario(nome, cpf, genero, setor, null);
					Salario salarioObj = new Salario(salarioBase, funcionarioParaDemonstrativo);
					funcionarioParaDemonstrativo.setSalario(salarioObj);

				} catch (FuncionarioException e) {
					System.out.println("Erro ao criar objeto Funcionário em memória: " + e.getMessage());
					e.printStackTrace();
					return;
				} catch (IllegalArgumentException e) {
					System.out.println(
							"Erro: Nome do setor '" + setorNomeStr + "' não corresponde a nenhum enum Setores.");
					return;
				}
				System.out.println();
				System.out.println("--- Demonstrativo Salarial ---");
				System.out.println();
				System.out.println("Funcionário: " + nome);
				System.out.println("ID do Funcionário: " + id);
				System.out.println("Setor: " + setorNomeStr);
				System.out.println("Salario base: R$ " + String.format("%.2f", salarioBase));
				System.out.println("Descontos (IR e INSS): R$ " + String.format("%.2f",
						(salarioBase - funcionarioParaDemonstrativo.getSalario().calculaSalario())));
				System.out.println("Salario final: R$ "
						+ String.format("%.2f", funcionarioParaDemonstrativo.getSalario().calculaSalario()));
				System.out.println("Beneficios");
				System.out.println(" - Plano de saúde: R$ "
						+ String.format("%.2f", funcionarioParaDemonstrativo.getSalario().getSaude()));
				System.out.println(" - Plano odontológico: R$ "
						+ String.format("%.2f", funcionarioParaDemonstrativo.getSalario().getOdonto()));
				System.out.println(" - Vale refeição/alimentação: R$ "
						+ String.format("%.2f", funcionarioParaDemonstrativo.getSalario().getValeRefAliment()));
				System.out.println("-------------------------------------");

			} else {
				System.out.println("Funcionário não encontrado.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao exibir demonstrativo do funcionário no banco de dados: " + e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	 public void calcularBonificacao(double lucroAnual, double nFuncionarios){

	        double bonificacao = (lucroAnual * 0.2) / nFuncionarios;
	        
	        if(bonificacao < 100) {
	        	System.out.println("Não há bonificação!");
	        	return;
	        }

	        System.out.println("Bonificação por funcionário: " + String.format("%.2f", bonificacao));
	    }
}