package Interfaces;

import Classes.Funcionario;
import Classes.Setor;

import java.util.ArrayList;
import java.util.Scanner;

public interface IntFuncionario {
    void menu();
    void cadastrarFuncionario(Scanner sc, ArrayList<Setor> setores, ArrayList<Funcionario> funcionarios);
    void listarFuncionariosPorSetor(Scanner sc, ArrayList<Setor> setores);
    void editarFuncionario(Scanner sc, Funcionario funcionarioEditar, ArrayList<Setor> setores);
    void excluirFuncionario(Scanner sc, ArrayList<Funcionario> funcionarios);
    Funcionario buscarFuncionarioPorId(String id, ArrayList<Funcionario> funcionarios);
}
