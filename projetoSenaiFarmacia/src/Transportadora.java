import java.util.ArrayList;
import java.util.List;

import Enums.Regiao;
import Validators.CnpjValidator;

public class Transportadora {
    private static int idBase = 1;
    private String nome;
    private int id;
    private String cnpj;
    private List<Regiao> regioes;

    public Transportadora(String nome, String cnpj) {
        if (!CnpjValidator.isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido para a transportadora " + nome + ": " + cnpj);
        }
        this.nome = nome;
        this.id = idBase;
        this.cnpj = cnpj;
        idBase++;
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

    public boolean atendeRegiao(Regiao regiaoBuscada){
        return this.regioes.contains(regiaoBuscada);
    };
}