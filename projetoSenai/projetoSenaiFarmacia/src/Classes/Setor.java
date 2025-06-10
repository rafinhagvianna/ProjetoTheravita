package Classes;
import java.util.ArrayList;
import Enums.Setores;

public class Setor {
    private String id;
    private Setores nome;
    private ArrayList<Funcionario> funcionarios;

    private static int proxId = 1;

    public Setor(Setores nome) {
        this.id = "SET"+proxId;
        proxId++;
        this.nome = nome;
        this.funcionarios = new ArrayList<>();
    }
    
    public Setor(Setores nome, ArrayList<Funcionario> funcionarios) {
        this.id = "SET"+proxId;
        proxId++;
        this.nome = nome;
        this.funcionarios = funcionarios;
    }

    public String getId(){
        return id;
    }

    public Setores getNome() {
        return nome;
    }

    public void setNome(Setores nome) {
        this.nome = nome;
    }

    public ArrayList<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public int funcionariosAtivos(){
        return funcionarios.size();
    }

    public static int totalFuncionarios(ArrayList<Setor> setores){
        int total = 0;

        for(Setor setor : setores){
            total += setor.funcionariosAtivos();
        }
        return total;
    }

    public void removerFuncionario(Funcionario funcionario){
        this.funcionarios.remove(funcionario);
    }

    public void adicionarFuncionario(Funcionario funcionario){
        this.funcionarios.add(funcionario);
    }

}


