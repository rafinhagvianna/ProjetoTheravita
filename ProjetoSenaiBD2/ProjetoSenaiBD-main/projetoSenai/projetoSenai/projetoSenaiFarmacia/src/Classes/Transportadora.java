package Classes;

import java.util.ArrayList;
import java.util.List;

import Enums.Regiao;

public class Transportadora {

    private int idDB;
    private String nome;
    private String cnpj;
    private List<Regiao> regioes;
    private double taxa;


    public Transportadora(int idDB, String nome, String cnpj, double taxa) {
        this.idDB = idDB;
        this.nome = nome;
        this.cnpj = cnpj;
        this.taxa = taxa;
        this.regioes = new ArrayList<>(); 
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdDB() {
        return idDB;
    }

    public double getTaxa() {
        return taxa;
    }

//    public String getCnpj() {
//        return cnpj;
//    }

    public void setCnpj(String cnpj) {
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
                "ID = " + idDB + 
                ", Nome = " + nome +
                ", CNPJ = " + cnpj +
                ", Taxa = " + taxa +
                ", Regiões atendidas =" + regioes;
    }
}