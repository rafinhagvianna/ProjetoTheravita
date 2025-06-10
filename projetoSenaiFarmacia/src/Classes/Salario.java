package Classes;
import java.time.LocalDate;
import java.util.ArrayList;

import Enums.Setores;

public class Salario {
    private double salario;
    private double saude;
    private double valeRefAliment;
    private double odonto;
    private Funcionario funcionario;

    public Salario() {

    }
    
    public Salario(double salario, Funcionario funcionario) {
        this.salario = salario;
        this.funcionario = funcionario;
        defineBeneficios();
    }


    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getSaude() {
        return saude;
    }

    public double getValeRefAliment() {
        return valeRefAliment;
    }

    public double getOdonto() {
        return odonto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        defineBeneficios();
    }

    public void defineBeneficios(){
        Setores setor = funcionario.getSetor().getNome();

        switch (setor) {
            case GERENTE_FILIAL:
                this.saude = 10000;
                this.valeRefAliment = 800;
                this.odonto = 10000;
                break;
            case ATENDIMENTO_CLIENTE:
                this.saude = 3000;
                this.valeRefAliment = 300;
                this.odonto = 3000;
                break;
            case GESTAO_PESSOAS:
                this.saude = 5000;
                this.valeRefAliment = 500;
                this.odonto = 5000;
                break;
            case FINANCEIRO:
                this.saude = 5000;
                this.valeRefAliment = 500;
                this.odonto = 5000;
                break;
            case VENDAS:
                this.saude = 3000;
                this.valeRefAliment = 300;
                this.odonto = 3000;
                break;
            case ALMOXARIFADO:
                this.saude = 3000;
                this.valeRefAliment = 300;
                this.odonto = 3000;
                break;
            default:
                break;
        }
    }

    public double calculaSalario(){
        double aliquota;
        double IR;
        double totalSalario;
        if (salario <= 2428.8) {
            aliquota = 0;
            IR = 0;
        }else if (salario <= 2826.65) {
            aliquota = 0.075;
            IR = 182.16;
        }else if (salario <= 3751.05) {
            aliquota = 0.15;
            IR = 394.16;
        }else if (salario <= 4664.68) {
            aliquota = 0.225;
            IR = 675.49;
        }else {
            aliquota = 0.275;
            IR = 908.75;
        }
        aliquota *= salario;

        totalSalario = salario - aliquota - IR;

        return totalSalario;
    }

    public double calcularBonificacao(double lucroAnual, double nFuncionarios){
        
        double bonificacao = (lucroAnual * 0.2) / nFuncionarios;

        return bonificacao;
    }

    public static double calcularBonificacao(Caixa caixa, ArrayList<Setor> setores){
        int anoAtual = LocalDate.now().getYear();
        double lucroAnual = caixa.lucroAnual(anoAtual);
        double nFuncionarios = Setor.totalFuncionarios(setores);
        double bonificacao;

        if (nFuncionarios != 0) {
            bonificacao = (lucroAnual * 0.2) / nFuncionarios;
        }else{
            return 0;
        }

        return bonificacao;
    }

}