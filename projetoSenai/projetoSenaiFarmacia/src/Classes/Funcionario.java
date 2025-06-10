package Classes;
import Validators.FuncionarioValidator;

public class Funcionario {
    private String nome;
    private String cpf;
    private String id;
    private String genero;
    private Setor setor;
    private Salario salario;

    private static int proxId = 1;

    public Funcionario() {
        this.id = "FUN"+proxId;
        proxId++;
    }
    
    public Funcionario(String nome, String cpf, String genero, Setor setor, Salario salario) throws FuncionarioException  {
        if (!FuncionarioValidator.isValidCpf(cpf)) {
            throw new FuncionarioException();
        }
        this.id = "FUN"+proxId;
        proxId++;
        this.nome = nome;
        this.genero = genero;
        this.cpf = cpf;
        this.setor = setor;
        this.salario = salario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws FuncionarioException {
        if (FuncionarioValidator.isValidCpf(cpf)) {
            this.cpf = cpf;
        } else {
            throw new FuncionarioException();
        }
    }

    public String getId() {
        return id;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Salario getSalario() {
        return salario;
    }

    public void setSalario(Salario salario) {
        this.salario = salario;
    }

    
    public void dadosFuncionario(){
        System.out.println("Funcionário: "+ nome);
        System.out.println("Setor: "+ setor.getNome());
        System.out.println("Salario base: "+ salario.getSalario());
        System.out.println("Descontos (IR e INSS): R$ " + (salario.getSalario() - salario.calculaSalario()));
        System.out.println("Salario final: "+ salario.calculaSalario());
        System.out.println("Beneficios");
        System.out.println(" - Plano de saúde: "+ salario.getSaude());
        System.out.println(" - Plano odontológico: "+ salario.getOdonto());
        System.out.println(" - Vale refeição/alimentação: "+ salario.getValeRefAliment());
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", id='" + id + '\'' +
                ", genero='" + genero + '\'' +
                ", setor=" + setor.getNome() +
                ", salario=" + salario.getSalario() +
                '}';
    }
}
