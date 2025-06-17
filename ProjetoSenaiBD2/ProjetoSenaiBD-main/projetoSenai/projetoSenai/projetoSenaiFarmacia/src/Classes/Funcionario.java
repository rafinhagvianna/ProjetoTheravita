package Classes;

import java.util.ArrayList;

import Exceptions.FuncionarioException;
import Validators.FuncionarioValidator;

public class Funcionario {
	private String nome;
	private String cpf;
	private String id;
	private String genero;
	private Setor setor;
	private Salario salario;

	public Funcionario() {

	}

	public Funcionario(String nome, String cpf, String genero, Setor setor, Salario salario)
			throws FuncionarioException {
		if (!FuncionarioValidator.isValidCpf(cpf) || !FuncionarioValidator.isValidNome(nome)
				|| !FuncionarioValidator.isValidGenero(genero)) {
			throw new FuncionarioException();
		}

		this.nome = nome;
		this.genero = genero;
		this.cpf = cpf;
		this.setor = setor;
		this.salario = salario;
	}

	public Funcionario(String id, String nome, String cpf, String genero, Setor setor, Salario salario)
			throws FuncionarioException {
		this(nome, cpf, genero, setor, salario);
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) throws FuncionarioException {
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) throws FuncionarioException {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public String getGenero() {
//		return genero;
//	}

	public void setGenero(String genero) throws FuncionarioException {
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Salario getSalario() {
		return salario;
	}

	public void setSalario(Salario salario) {
		this.salario = salario;
	}

//	public void dadosFuncionario() {
//		System.out.println("Funcionário: " + nome);
//		System.out.println("Setor: " + setor.getNome());
//		System.out.println("Salario base: " + salario.getSalario());
//		System.out.println("Descontos (IR e INSS): R$ " + (salario.getSalario() - salario.calculaSalario()));
//		System.out.println("Salario final: " + salario.calculaSalario());
//		System.out.println("Beneficios");
//		System.out.println(" - Plano de saúde: " + salario.getSaude());
//		System.out.println(" - Plano odontológico: " + salario.getOdonto());
//		System.out.println(" - Vale refeição/alimentação: " + salario.getValeRefAliment());
//	}

	@Override
	public String toString() {
		String salarioStr = (salario != null) ? "Salário: " + salario.getSalario() : "Salário não definido";
		return "Nome: " + nome + "\nCPF: " + cpf + "\nGênero: " + genero + "\nSetor: " + setor.getNome() + "\n"
				+ salarioStr + "\nID: " + id;
	}

//	public static Funcionario buscarFuncionarioPorId(String id, ArrayList<Funcionario> funcionarios) {
//		for (Funcionario funcionario : funcionarios) {
//			if (funcionario.getId().equals(id)) {
//				return funcionario;
//			}
//		}
//		return null;
//	}
}