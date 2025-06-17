package Classes;

import Enums.Setores;
import Exceptions.FuncionarioException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Gerenciadores.*;

public class Main {
//	static Scanner scanner = new Scanner(System.in);
	static ArrayList<Funcionario> funcionarios = new ArrayList<>();
	static ArrayList<Setor> setores = new ArrayList<>();
	static ArrayList<Produto> produtos = new ArrayList<>();
	static ArrayList<Transportadora> transportadoras = new ArrayList<>();
	static Caixa caixa = new Caixa();

	static {
		setores.add(new Setor(Setores.ALMOXARIFADO));
		setores.add(new Setor(Setores.ATENDIMENTO_CLIENTE));
		setores.add(new Setor(Setores.FINANCEIRO));
		setores.add(new Setor(Setores.GERENTE_FILIAL));
		setores.add(new Setor(Setores.GESTAO_PESSOAS));
		setores.add(new Setor(Setores.VENDAS));
	}

	public static void main(String[] args) {
		System.out.println("Bem vindo ao programa!");
		apresentarMenu();
	}

	public static void apresentarMenu() {
		char opcaoInicialUsuario;
		do {

			Scanner scannerMenuPrincipal = new Scanner(System.in);
			System.out.println("______________________________");
			System.out.println("|  ESCOLHA UMA DAS OPÇÕES:   |");
			System.out.println("|____________________________|");
			System.out.println("|____________________________|");
			System.out.println("|     FUNCIONÁRIOS - (F)     |");
			System.out.println("|  SALÁRIO E BENEFÍCIOS - (S)|");
			System.out.println("|      PRODUTO - (P)         |");
			System.out.println("|        CAIXA - (C)         |");
			System.out.println("|   TRANSPORTADORAS - (T)    |");
			System.out.println("|   GESTÃO DE NEGÓCIOS - (G) |");
			System.out.println("|____________________________|");
			System.out.println("|____________________________|");
			System.out.println("|         SAIR - (E)         |");
			System.out.println("|____________________________|");

			opcaoInicialUsuario = scannerMenuPrincipal.next().toUpperCase().charAt(0);

			switch (opcaoInicialUsuario) {
			case 'F':
				try {
					apresentarMenuFuncionarios(scannerMenuPrincipal);
				} catch (FuncionarioException e) {
					e.printStackTrace();
				}
				break;
			case 'S':
				apresentarMenuSalarios(scannerMenuPrincipal, funcionarios);
				break;
			case 'P':
				apresentarMenuProduto(scannerMenuPrincipal);
				break;
			case 'C':
				apresentarMenuCaixa(scannerMenuPrincipal);
				break;
			case 'T':
				apresentarMenuTransportadoras(scannerMenuPrincipal, transportadoras);
				break;
			case 'G':
				apresentarMenuGestao(scannerMenuPrincipal);
				break;
			case 'E':
				System.out.println("Obrigado por utilizar o nosso sistema!");
				break;
			default:
				System.out.println("\nOpção inválida!\n");
				break;
			}
		} while (opcaoInicialUsuario != 'E');
	}

	public static void apresentarMenuFuncionarios(Scanner scanner) throws FuncionarioException {
		int opcaoUsuarioFuncionario;
		GerFuncionario gerFuncionario = new GerFuncionario();
		do {
			try {
				gerFuncionario.menu();
				opcaoUsuarioFuncionario = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				scanner.nextLine();
				opcaoUsuarioFuncionario = 6;
			}

			switch (opcaoUsuarioFuncionario) {
			case 1:
				gerFuncionario.cadastrarFuncionario(scanner, setores, funcionarios);
				break;
			case 2:
				gerFuncionario.listarFuncionariosPorSetor(scanner, setores);
				break;
			case 3:
				gerFuncionario.editarFuncionario(scanner, setores);
				break;
			case 4:
				System.out.print("Informe o CPF do funcionário a ser removido: ");
				gerFuncionario.excluirFuncionario(scanner, funcionarios);
				break;
			case 5:
				int totalFuncionarios = GerFuncionario.getTotalFuncionarios();
				System.out.println("Total de funcionários cadastrados: " + totalFuncionarios);
				break;
			case 0:
				System.out.println("Retornando ao menu principal...");
				break;
			default:
				System.out.println("Opção inválida! Tente novamente.");
				break;
			}
		} while (opcaoUsuarioFuncionario != 0);
	}

	public static void apresentarMenuSalarios(Scanner scanner, ArrayList<Funcionario> funcionarios) {
		int opcaoUsuarioSalario;
		GerSalario gerSalario = new GerSalario();
		do {
			gerSalario.menu();

			try {
				opcaoUsuarioSalario = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException inputExcpt) {
				scanner.nextLine();
				opcaoUsuarioSalario = 6;
			}

			switch (opcaoUsuarioSalario) {
			case 1:
				gerSalario.salarioBruto(funcionarios);
				break;
			case 2:
				gerSalario.calcularSalario(funcionarios);
				break;
			case 3:
				gerSalario.consultarBeneficios(funcionarios);
				break;
			case 4:
				gerSalario.exibirDemonstrativo(funcionarios);
				break;
			case 5:
				GerCaixa gerCaixa = new GerCaixa();

				int anoBonificacao = 0;
				do {
					try {
						System.out.println("Digite o ano para o cálculo da bonificação (YYYY): ");
						anoBonificacao = scanner.nextInt();
						scanner.nextLine(); // Consumir a quebra de linha
					} catch (InputMismatchException e) {
						System.out.println("Entrada inválida! Por favor, digite um número inteiro para o ano.");
						scanner.nextLine(); // Consumir a entrada inválida
						anoBonificacao = 0; // Resetar para continuar o loop
					}
					if (anoBonificacao < 1900 || anoBonificacao > 2100) { // Exemplo de validação de ano
						System.out.println("Ano fora do período razoável. Tente novamente.");
						anoBonificacao = 0;
					}
				} while (anoBonificacao == 0);

				double lucroAnualParaBonificacao = gerCaixa.calcularLucroAnualNoBanco(anoBonificacao);

				int totalFuncionariosParaBonificacao = GerFuncionario.getTotalFuncionarios();

				gerSalario.calcularBonificacao(lucroAnualParaBonificacao, totalFuncionariosParaBonificacao);
				break;
			case 0:
				System.out.println("Retornando ao menu principal...");
				break;
			default:
				System.out.println("Opção inválida! Tente novamente.");
			}
		} while (opcaoUsuarioSalario != 0);
	}

	public static void apresentarMenuProduto(Scanner scanner) {
		int opcaoUsuarioProduto;
		GerProduto gerProduto = new GerProduto();
		do {
			gerProduto.menu();
			try {
				opcaoUsuarioProduto = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException inputExcpt) {
				scanner.nextLine();
				opcaoUsuarioProduto = 6;
			}

			switch (opcaoUsuarioProduto) {
			case 1:
				gerProduto.cadastraProduto();
				break;
			case 2:
				gerProduto.listarProdutos();
				break;
			case 3:
				gerProduto.atualizarProduto();
				break;
			case 4:
				gerProduto.disponibilidadeProduto();
				break;
			case 0:
				System.out.println("Retornando ao menu principal...");
				break;
			default:
				System.out.println("Opção inválida! Tente novamente.");
			}
		} while (opcaoUsuarioProduto != 0);
	}

	public static void apresentarMenuCaixa(Scanner scanner) {
		int opcaoUsuarioCaixa;
		GerCaixa gerCaixa = new GerCaixa();
		do {
			gerCaixa.menu();
			try {
				opcaoUsuarioCaixa = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException inputExcpt) {
				scanner.nextLine();
				opcaoUsuarioCaixa = 6;
			}

			switch (opcaoUsuarioCaixa) {
			case 1:
				Venda novaVenda = new Venda();
				gerCaixa.registrarEntrada(caixa, novaVenda, funcionarios, transportadoras, setores, produtos);
				break;
			case 2:
				Compra novaCompra = new Compra();
				gerCaixa.registrarSaida(caixa, novaCompra, funcionarios, produtos);
				break;
			case 3:
				System.out.println("Saldo atual: " + caixa.getSaldo());
				break;
			case 4:
				int mes = 0;
				int ano = 0;

				do {
					try {
						System.out.println("Digite o mês (1-12): ");
						mes = scanner.nextInt();
						scanner.nextLine();
					} catch (Exception e) {
						System.out.println("Tipo inserido inválido!");
					}
				} while (mes < 1 || mes > 12);

				do {
					try {
						System.out.println("Digite o ano: ");
						ano = scanner.nextInt();
						scanner.nextLine();
					} catch (Exception e) {
						System.out.println("Tipo inserido inválido!");
					}
				} while (ano < 2000 || ano > 2050);

				double lucroMensal = caixa.lucroMensal(mes, ano);
				System.out.println("Lucro do mês: " + String.format("%.2f", lucroMensal));
				break;
			case 5:
				int anoAnual = 0;
				do {
					try {
						System.out.println("Digite o ano: ");
						anoAnual = scanner.nextInt();
						scanner.nextLine();
					} catch (Exception e) {
						System.out.println("Tipo inserido inválido!");
					}
				} while (anoAnual < 2000 || anoAnual > 2050);

				double lucroAnual = caixa.lucroAnual(anoAnual);
				System.out.println("Lucro do ano: " + String.format("%.2f", lucroAnual));
				break;
			case 6:
				gerCaixa.gerarRelatorio(caixa);
				break;
			case 0:
				System.out.println("Saindo do menu do caixa.");
				break;
			default:
				System.out.println("Opção inválida, tente novamente.");
				break;
			}
		} while (opcaoUsuarioCaixa != 0);
	}

	public static void apresentarMenuTransportadoras(Scanner scanner, ArrayList<Transportadora> transportadoras) {
		int opcaoUsuarioTransportadoras;
		GerTransportadora gerTransportadora = new GerTransportadora();
		do {
			gerTransportadora.menu();
			try {
				opcaoUsuarioTransportadoras = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException inputExcpt) {
				opcaoUsuarioTransportadoras = 6;
			}

			switch (opcaoUsuarioTransportadoras) {
			case 1:
				gerTransportadora.cadastrarTransportadora(scanner, transportadoras);
				break;
			case 2:
				gerTransportadora.listarTransportadoras(transportadoras);
				break;
			case 3:
				gerTransportadora.atualizarTransportadora(scanner, transportadoras);
				break;
			case 4:
				gerTransportadora.visualizarTotalTransportadoras(transportadoras);
				break;
			case 0:
				System.out.println("Saindo do menu de transportadoras.");
				break;
			default:
				System.out.println("Opção inválida, tente novamente.");
				break;
			}
		} while (opcaoUsuarioTransportadoras != 0);
	}

	public static void apresentarMenuGestao(Scanner scanner) {
		int opcaoSelecionada;
		GerGestao gerGestao = new GerGestao();
		do {
			System.out.println("\nEscolha uma das opções: ");
			System.out.println("--------------------------------------------");
			System.out.println("| 1 - Consultar negócios em andamento      |");
			System.out.println("| 2 - Atualizar status                     |");
			System.out.println("| 0 - Sair                                 |");
			System.out.println("--------------------------------------------");
			System.out.print("Opção: ");
			try {
				opcaoSelecionada = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Entrada inválida! Digite um número.");
				scanner.nextLine();
				opcaoSelecionada = -1;
			}

			switch (opcaoSelecionada) {
			case 1:
				gerGestao.consultarNegocios();
				break;
			case 2:
				gerGestao.atualizarStatus(scanner);
				break;
			case 0:
				System.out.println("Retornando ao menu principal...");
				break;
			default:
				if (opcaoSelecionada != -1) {
					System.out.println("Opção inválida. Tente novamente.");
				}
				break;
			}
		} while (opcaoSelecionada != 0);
	}
}