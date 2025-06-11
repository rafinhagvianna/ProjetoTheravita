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
    private static ArrayList<Transportadora> transportadoras = new ArrayList<>();

    public Transportadora(String nome, String cnpj, double taxa) {
        if (!CnpjValidator.isValid(cnpj, transportadoras)) {
            throw new IllegalArgumentException("CNPJ inválido para a transportadora " + nome + ": " + cnpj);
        }
        this.nome = nome;
        this.id = "TRAN" + idBase;
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
        if (!CnpjValidator.isValid(cnpj, transportadoras)) {
            throw new IllegalArgumentException("CNPJ inválido: " + cnpj);
        }
        this.cnpj = cnpj;
    }

    public List<Regiao> getRegiao() {
        return this.regioes;
    }

    public void setRegiao(Regiao regiao) {
        if (!this.regioes.contains(regiao)) {
            this.regioes.add(regiao);
        }
    }

    public void removerRegiao(Regiao regiao) {
        this.regioes.remove(regiao);
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public boolean atendeRegiao(Regiao regiaoBuscada) {
        return this.regioes.contains(regiaoBuscada);
    }

    @Override
    public String toString() {
        return "Transportadora: " +
                "Nome = " + nome +
                ", id = " + id +
                ", cnpj = " + cnpj +
                ", taxa = " + String.format("%.2f", taxa) +
                ", Regiões atendidas =" + regioes;
    }

    public static ArrayList<Transportadora> getTransportadoras() {
        return transportadoras;
    }
    public static void setTransportadoras(Transportadora transportadora) {
        transportadoras.add(transportadora);
    }

    public static Transportadora buscarTransportadora(String cnpj) {
        for (Transportadora transportadora : transportadoras) {
            if (transportadora.getCnpj().equals(cnpj)) {
                return transportadora;
            }
        }
        return null;
    }
}