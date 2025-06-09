package Classes;
import java.util.ArrayList;
import java.util.List;

import Enums.Regiao;
import Validators.CnpjValidator;

public class Transportadora {
    private static int idBase = 1;
    private String nome;
    private String id;
    private String cnpj;
    private List<Regiao> regioes;
    private double taxa;
    private static List<Transportadora> transportadoras = new ArrayList<>();

    public Transportadora(String nome, String cnpj, double taxa) {
        if (!CnpjValidator.isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido para a transportadora " + nome + ": " + cnpj);
        }
        this.nome = nome;
        this.id = "TRA" + idBase;
        this.cnpj = cnpj;
        this.taxa = taxa;
        idBase++;
        this.regioes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }
    public double getTaxa() {
        return taxa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (!CnpjValidator.isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido: " + cnpj);
        }
        this.cnpj = cnpj;
    }

    public void getRegiao() {
        for (Regiao r : regioes) {
            System.out.println(r);
        }
    }

    public void setRegiao(Regiao regiao) {
        this.regioes.add(regiao);
    }

    public void setTaxa(double taxa){
        this.taxa = taxa;
    }

    public boolean atendeRegiao(Regiao regiaoBuscada){
        return this.regioes.contains(regiaoBuscada);
    }

    @Override
    public String toString() {
        return "Transportadora: " +
                "Nome = " + nome +
                ", id = " + id +
                ", cnpj = " + cnpj +
                ", Regiões atendidas =" + regioes;
    }

    public static void getTransportadoras() {
        if (transportadoras == null) {
            System.out.println("Não há transportadoras cadastradas.");
        }

        for (Transportadora t : transportadoras) {
            System.out.println(t);
        }
    }
}