public class Funcionario {
    private String nome;
    private int cpf;
    private int id;
    private String genero;
    private Setor setor;
    private Salario salario;

    public Funcionario() {

    }

    public Funcionario(String nome, int cpf, int id, String genero, Setor setor, Salario salario) {
        this.nome = nome;
        this.cpf = cpf;
        this.id = id;
        this.genero = genero;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
