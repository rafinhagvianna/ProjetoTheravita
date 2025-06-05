import java.util.ArrayList;

public class Setor {
    private String nome;
    private ArrayList<Funcionario> funcionarios;

    public Setor() {

    }

    public Setor(String nome, ArrayList<Funcionario> funcionarios) {
        this.nome = nome;
        this.funcionarios = funcionarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Funcionario> getFuncionarios() {
        return funcionarios;
    }

}


