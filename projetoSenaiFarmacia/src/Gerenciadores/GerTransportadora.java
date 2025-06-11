package Gerenciadores;

import java.util.ArrayList;
import java.util.Scanner;

import Classes.*;
import Enums.Regiao;
import Interfaces.IntTransportadora;

public class GerTransportadora implements IntTransportadora {
    Scanner sc = new Scanner(System.in);

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

    // Método para cadastrar uma nova transportadora
    @Override
    public void cadastrarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        System.out.println("Digite o nome da transportadora: ");
        String nome = scanner.nextLine();

        System.out.println("Digite o CNPJ da transportadora: ");
        String cnpj = scanner.nextLine();

        double taxa = -1;
        do {
            try {
                System.out.print("Digite a taxa da transportadora (% em cima da venda cobrado): ");
                taxa = scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("Tipo inserido inválido. Digite um valor real!");
                taxa = -1;
            }

        } while (taxa < 0);

        Regiao regiao = null;
        do {
            try {
                Regiao[] regioes = Regiao.values();
                int i = 1;
                for (Regiao r : regioes) {
                    System.out.println(i + " - " + r);
                    i++;
                }
                System.out.println();
                System.out.println("Digite a região");
                int regiaoEscolhida = scanner.nextInt();
                regiao = regioes[regiaoEscolhida - 1];
            } catch (Exception e) {
                System.out.println("Opção inválida! Tente novamente.");
                regiao = null;
            }
        } while (regiao == null);

        try {
            Transportadora novaTransportadora = new Transportadora(nome, cnpj, taxa);
            novaTransportadora.setRegiao(regiao);
            transportadoras.add(novaTransportadora);
            System.out.println("Transportadora cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar transportadora: " + e.getMessage());
        }
    }

    // Método para listar todas as transportadoras
    public void listarTransportadoras(ArrayList<Transportadora> transportadoras) {
        if (transportadoras.isEmpty()) {
            System.out.println("Nenhuma transportadora cadastrada.");
        } else {
            System.out.println("Lista de transportadoras:");
            for (Transportadora t : transportadoras) {
                System.out.println(t);
            }
        }
    }

    // Método para atualizar dados de uma transportadora
    public void atualizarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        Regiao[] regioes = Regiao.values();
        while (transportadoras.isEmpty()) {
            System.out.println("Nenhuma transportadora cadastrada.");
            return;

        }

        String idAtualizacao;
        Transportadora transportadoraEncontrada = null;

        do {
            System.out.println("Digite o ID da transportadora que deseja atualizar: ");
            idAtualizacao = scanner.nextLine();

            transportadoraEncontrada = Transportadora.buscarTransportadora(idAtualizacao);

            if (transportadoraEncontrada == null) {
                System.out.println("ID inválido!");
                System.out.println("Transportadora não encontrada.");
                return;
            }

        } while (transportadoraEncontrada == null);

        int opc;
        do {
            System.out.println(
                    "Qual dado deseja modificar? \n1 - Nome\n2 - CNPJ\n3 - Taxa\n4 - Adicionar Região\n5 - Remover Região\n0 - Sair");

            try {
                opc = scanner.nextInt();
                scanner.nextLine(); // Evita erro de leitura

                switch (opc) {

                    case 1:
                        System.out.println("Digite o novo nome da transportadora: ");
                        String novoNome = scanner.nextLine();
                        transportadoraEncontrada.setNome(novoNome);
                        System.out.println("Nome atualizado com sucesso!");
                        break;

                    case 2:
                        System.out.println("Digite o novo CNPJ da transportadora: ");
                        String novoCnpj = scanner.nextLine();
                        transportadoraEncontrada.setCnpj(novoCnpj);
                        System.out.println("CNPJ atualizado com sucesso!");
                        break;

                    case 3:
                        double novaTaxa = -1;
                        do {
                            try {
                                System.out.print("Digite a taxa da transportadora (% em cima da venda cobrado): ");
                                novaTaxa = scanner.nextDouble();
                            } catch (Exception e) {
                                System.out.println("Tipo inserido inválido. Digite um valor real!");
                                novaTaxa = -1;
                            }

                        } while (novaTaxa < 0);
                        transportadoraEncontrada.setTaxa(novaTaxa);
                        System.out.println("Taxa atualizada com sucesso!");
                        break;

                    case 4:
                        System.out.println("Adicione a região: ");

                        Regiao regiao = null;
                        do {
                            try {
                                int i = 1;
                                for (Regiao r : regioes) {
                                    System.out.println(i + " - " + r);
                                    i++;
                                }
                                System.out.println();
                                System.out.println("Digite a região");
                                int regiaoEscolhida = scanner.nextInt();
                                regiao = regioes[regiaoEscolhida - 1];
                            } catch (Exception e) {
                                System.out.println("Opção inválida! Tente novamente.");
                                regiao = null;
                            }
                        } while (regiao == null);

                        transportadoraEncontrada.setRegiao(regiao);
                        System.out.println("Região adicionada com sucesso!");
                        break;

                    case 5:
                        System.out.println("Remova a região: ");
                        
                        Regiao removerRegiao = null;
                        do {
                            try {
                                int i = 1;
                                for (Regiao r : transportadoraEncontrada.getRegiao()) {
                                    System.out.println(i + " - " + r);
                                    i++;
                                }
                                System.out.println();
                                System.out.println("Digite a região");
                                int regiaoEscolhida = scanner.nextInt();
                                removerRegiao = transportadoraEncontrada.getRegiao().get(regiaoEscolhida - 1);
                            } catch (Exception e) {
                                System.out.println("Opção inválida! Tente novamente.");
                                removerRegiao = null;
                            }
                        } while (removerRegiao == null);

                        transportadoraEncontrada.removerRegiao(removerRegiao);
                        System.out.println("Região removida com sucesso!");
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida! Tente novamente.");
                scanner.nextLine(); // Evita loop infinito
                opc = -1;
            }

        } while (opc != 0);
    }

    // Método para visualizar o total de transportadoras cadastradas
    public void visualizarTotalTransportadoras(ArrayList<Transportadora> transportadoras) {
        System.out.println("Total de transportadoras cadastradas: " + transportadoras.size());
    }

}
