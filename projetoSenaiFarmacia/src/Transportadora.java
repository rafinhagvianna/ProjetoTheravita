import java.util.ArrayList;
import java.util.List;

import Enums.Regiao;
import Validators.CnpjValidator;

public class Transportadora {
    private String nome;
    private int id;
    private String cnpj;
    private List<Regiao> regioes;

    public Transportadora(String nome, int id, String cnpj) {
        if (!CnpjValidator.isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido para a transportadora " + nome + ": " + cnpj);
        }
        this.nome = nome;
        this.id = id;
        this.cnpj = cnpj;
        this.regioes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void getRegioes() {
        for (Regiao r : regioes) {
            System.out.println(r);
        }
    }

    public void setRegiao(Regiao regiao) {
        this.regioes.add(regiao);
    }

    public boolean atendeRegiao(Regiao regiaoBuscada){
        return this.regioes.contains(regiaoBuscada);
    }
}
