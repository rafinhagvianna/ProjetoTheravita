import java.util.ArrayList;

public class Setor {
    private String nome;
    private ArrayList<Funcionario> qtdFuncionarios;

    public Setor() {

    }

    public Setor(String nome, ArrayList<Funcionario> qtdFuncionarios) {
        this.nome = nome;
        this.qtdFuncionarios = qtdFuncionarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Funcionario> getQtdFuncionarios() {
        return qtdFuncionarios;
    }

}


