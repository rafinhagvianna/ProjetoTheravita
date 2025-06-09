package Gerenciadores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Classes.*;
import Enums.Regiao;
import Interfaces.IntCaixa;

public class GerCaixa implements IntCaixa {
    Scanner scanner = new Scanner(System.in);
    public void menu(){
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

    public void registrarEntrada(Caixa caixa, Venda novaVenda, ArrayList<Funcionario> funcionarios, ArrayList<Transportadora>  transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos){
        novaVenda = realizarVenda(scanner, funcionarios, transportadoras, setores, produtos);
        caixa.getEntrada().add(novaVenda);
        System.out.println("Venda registrada com sucesso!");
    }

    public static Venda realizarVenda(Scanner scanner, ArrayList<Funcionario> funcionarios, ArrayList<Transportadora>  transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos){
        int prod, quantidade;
        Venda venda = new Venda();
        Funcionario funcionarioVenda = null;
        Regiao regiao;
        Transportadora transportadora = null;
        ArrayList<Itens> itens = new ArrayList<>();

        System.out.println("Iniciando nova venda...");


        do{
            System.out.println("Insira o id do produto ou 0 para parar: ");
            prod = scanner.nextInt();

            if (prod != 0 ) {
                Produto produto = buscarProdutoPorId(prod, produtos);

                System.out.println("Quantas unidades do produto "+ produto.getDescricao() +" deseja adicionar? (0 para cancelar)");
                quantidade = scanner.nextInt();

                if (quantidade != 0 ) {
                    Itens item = new Itens(quantidade, produto);
                    itens.add(item);
                }
            }
        }while(false);

        if (itens.size() > 0) {
            venda.setProdutos(itens);

            do {
                System.out.print("Informe o ID do funcionário: ");
                String idFuncionario = scanner.next();
                funcionarioVenda = buscarFuncionarioPorId(idFuncionario, funcionarios);

                if (funcionarioVenda != null) {
                    venda.setFuncionario(funcionarioVenda);
                }else {
                    System.out.println("\nFuncionário não encontrado !\n");
                }
            } while (funcionarioVenda == null);


            System.out.println("Digite a data da venda (AAAA-MM-DD) ou HJ para dia de hoje: ");
            String dataVenda = scanner.nextLine();
            LocalDate dtVenda = LocalDate.parse(dataVenda);
            if (dataVenda.equals("HJ")) {
                dtVenda = LocalDate.now();
            }else {
                dtVenda = LocalDate.parse(dataVenda);
            }
            venda.setData(dtVenda);

            if (dtVenda.isAfter(LocalDate.now())) {
                venda.setStatus(Enums.Status.ABERTO);
            }else {
                venda.setStatus(Enums.Status.FECHADO);;
            }

            do{
                System.out.println("Escolha a região de entrega da venda:");
                Regiao[] regioes = Regiao.values();
                for (int i = 0; i < regioes.length; i++) {
                    System.out.println((i + 1) + " - " + regioes[i]);
                }
                int regiaoEscolhida = scanner.nextInt();
                try{
                    regiao = regioes[regiaoEscolhida - 1];
                }catch (ArrayIndexOutOfBoundsException | InputMismatchException e){
                    System.out.println("Opção inválida! Tente novamente.");
                    scanner.nextLine();
                    regiao = null;
                }
            }while(regiao != null);

            do {
                System.out.println("Escolha a transportadora para a venda:");
                for (int i = 0; i < transportadoras.size(); i++) {
                    System.out.println((i + 1) + " - " + setores.get(i).getNome());
                }
                int transportadoraEscolhida = scanner.nextInt();

                try{
                    transportadora = transportadoras.get(transportadoraEscolhida);
                }catch (ArrayIndexOutOfBoundsException | InputMismatchException e){
                    System.out.println("Opção inválida! Tente novamente.");
                    scanner.nextLine();
                    transportadora = null;
                }


                if (!transportadora.atendeRegiao(regiao) && transportadora != null ) {
                    System.out.println("Transportadora não atende região definida!");
                    transportadora = null;
                }
            } while (transportadora == null);

            venda.setTransportadora(transportadora);

            venda.setValor(venda.calculaTotal());

            return venda;
        }else{
            System.out.println("Nenhum produto foi definido!");

            return null;
        }

    }

    public static Funcionario buscarFuncionarioPorId(String id, ArrayList<Funcionario> funcionarios) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId().equals(id)) {
                return funcionario;
            }
        }
        return null;
    }

    public static Produto buscarProdutoPorId(int id, ArrayList<Produto> produtos){
        for(Produto produto : produtos){
            if (produto.getId().equals(id)) {
                return produto;
            }
        }
        return null;
    }
}
