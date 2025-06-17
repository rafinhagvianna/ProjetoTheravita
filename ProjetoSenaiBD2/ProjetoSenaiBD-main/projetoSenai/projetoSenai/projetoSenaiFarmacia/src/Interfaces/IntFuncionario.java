package Interfaces;

import Classes.Funcionario;
import Classes.Salario;
import Classes.Setor;


import java.util.ArrayList;
import java.util.Scanner;

public interface IntFuncionario {
    void menu();
    void cadastrarFuncionario(Scanner scanner, ArrayList<Setor> setores, ArrayList<Funcionario> funcionarios);
    void listarFuncionariosPorSetor(Scanner scanner, ArrayList<Setor> setores);
    void editarFuncionario(Scanner scanner, ArrayList<Setor> setores);
    void excluirFuncionario(Scanner sc, ArrayList<Funcionario> funcionarios);
    void carregarFuncionariosDoBanco(ArrayList<Funcionario> funcionarios, ArrayList<Setor> setores);

    
    
}

