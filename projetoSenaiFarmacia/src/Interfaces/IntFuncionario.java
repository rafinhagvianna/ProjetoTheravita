package Interfaces;

import Classes.Funcionario;
import Classes.Setor;

import java.util.ArrayList;

public interface IntFuncionario {
    void menu();
    void cadastrarFuncionario(ArrayList<Setor> setores, ArrayList<Funcionario> funcionarios);
    void listarFuncionariosPorSetor(ArrayList<Setor> setores);
    void editarFuncionario(Funcionario funcionarioEditar, ArrayList<Setor> setores);
    void excluirFuncionario(ArrayList<Funcionario> funcionarios);
    Funcionario buscarFuncionarioPorId(String id, ArrayList<Funcionario> funcionarios);
}
