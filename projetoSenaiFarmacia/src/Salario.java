public class Salario {
    private double salario;
    private double valeTransporte;
    private double valeRefAliment;
    private double odonto;
    private double bonificacao;
    private Funcionario funcionario;

    public Salario() {

    }

    public Salario(double salario, double valeTransporte, double valeRefAliment, double odonto, double bonificacao, Funcionario funcionario) {
        this.salario = salario;
        this.valeTransporte = valeTransporte;
        this.valeRefAliment = valeRefAliment;
        this.odonto = odonto;
        this.bonificacao = bonificacao;
        this.funcionario = funcionario;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getValeTransporte() {
        return valeTransporte;
    }

    public void setValeTransporte(double valeTransporte) {
        this.valeTransporte = valeTransporte;
    }

    public double getValeRefAliment() {
        return valeRefAliment;
    }

    public void setValeRefAliment(double valeRefAliment) {
        this.valeRefAliment = valeRefAliment;
    }

    public double getOdonto() {
        return odonto;
    }

    public void setOdonto(double odonto) {
        this.odonto = odonto;
    }

    public double getBonificacao() {
        return bonificacao;
    }

    public void setBonificacao(double bonificacao) {
        this.bonificacao = bonificacao;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
