import java.util.ArrayList;
import java.util.Scanner;

public class genTransportadora{

    public static void cadastrarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        System.out.println("Digite o nome da transportadora: ");
        String nome = scanner.nextLine();

        System.out.println("Digite o ID da transportadora: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Digite o CNPJ da transportadora: ");
        String cnpj = scanner.nextLine();

        try {
            Transportadora novaTransportadora = new Transportadora(nome, id, cnpj);
            transportadoras.add(novaTransportadora);
            System.out.println("Transportadora cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar transportadora: " + e.getMessage());
        }
    }

    public static void listarTransportadoras(ArrayList<Transportadora> transportadoras) {
        if (transportadoras.isEmpty()) {
            System.out.println("Nenhuma transportadora cadastrada.");
        } else {
            System.out.println("Lista de transportadoras:");
            for (Transportadora t : transportadoras) {
                System.out.println("ID: " + t.getId() + ", Nome: " + t.getNome() + ", CNPJ: " + t.getCnpj());
            }
        }
    }

    public static void atualizarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadoras) {
        System.out.println("Digite o ID da transportadora que deseja atualizar: ");
        int idAtualizacao = scanner.nextInt();
        scanner.nextLine();

        Transportadora transportadoraEncontrada = null;
        for (Transportadora t : transportadoras) {
            if (t.getId() == idAtualizacao) {
                transportadoraEncontrada = t;
                break;
            }
        }

        if (transportadoraEncontrada != null) {
            System.out.println("Digite o novo nome da transportadora: ");
            String novoNome = scanner.nextLine();
            transportadoraEncontrada.setNome(novoNome);

            System.out.println("Digite o novo CNPJ da transportadora: ");
            String novoCnpj = scanner.nextLine();
            try {
                transportadoraEncontrada.setCnpj(novoCnpj);
                System.out.println("Transportadora atualizada com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Transportadora com ID " + idAtualizacao + " não encontrada.");
        }
    }

    public static void visualizarTotalTransportadoras(ArrayList<Transportadora> transportadoras) {
        System.out.println("Total de transportadoras cadastradas: " + transportadoras.size());
    }
}
