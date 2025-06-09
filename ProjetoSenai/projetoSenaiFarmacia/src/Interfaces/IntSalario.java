package Interfaces;

import Classes.Funcionario;
import Classes.Caixa;
import Classes.Setor;

import java.util.ArrayList;

public interface IntSalario {
    void menu();
    void salarioBruto(ArrayList<Funcionario> funcionarios);
    void calcularSalario(ArrayList<Funcionario> funcionarios);
    void consultarBeneficios(ArrayList<Funcionario> funcionarios);
    void valorBonificacao(Caixa caixa, ArrayList<Setor> setores);
}
