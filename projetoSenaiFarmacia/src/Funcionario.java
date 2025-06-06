import Validators.CpfValidator;

public class Funcionario {
    private String nome;
    private int cpf;
    private String id;
    private String genero;
    private Setor setor;
    private Salario salario;

    private static int proxId = 1;

    public Funcionario() {
        this.id = "FUN"+proxId;
        proxId++;
    }
    
    public Funcionario(String nome, int cpf, String genero, Setor setor, Salario salario) {
        if (!CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido para a transportadora " + nome + ": " + cpf);
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

    public int getCpf() {
        return cpf;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
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
        System.out.println("Salario final: "+ salario.calculaSalario());
        System.out.println("Beneficios");
        System.out.println(" - Plano de saúde: "+ salario.getSaude());
        System.out.println(" - Plano odontológico: "+ salario.getOdonto());
        System.out.println(" - Vale refeição/alimentação: "+ salario.getValeRefAliment());
    }
}
