import java.util.ArrayList;

import Enums.Regiao;

public class Transportadora {

    private String nome;
    private int id;
    private int cnpj;
    private ArrayList<Regiao> regiao;

    public Transportadora(){

    }

    public Transportadora(String nome, int id, int cnpj) {
        this.nome = nome;
        this.id = id;
        this.cnpj = cnpj;
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

    public int getCnpj() {
        return cnpj;
    }

    public void setCnpj(int cnpj) {
        this.cnpj = cnpj;
    }

    public ArrayList<Regiao> getRegiao() {
        return regiao;
    }

    public void setRegiao(ArrayList<Regiao> regiao) {
        this.regiao = regiao;
    }

    public boolean atendeRegiao(Regiao regiao){

        return true;
    };
}
