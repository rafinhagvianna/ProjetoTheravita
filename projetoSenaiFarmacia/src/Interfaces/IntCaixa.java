package Interfaces;

import Classes.*;

import java.util.ArrayList;

public interface IntCaixa {
    void menu();
    void registrarEntrada(Caixa caixa, Venda novaVenda, ArrayList<Funcionario> funcionarios,
                          ArrayList<Transportadora> transportadoras, ArrayList<Setor> setores, ArrayList<Produto> produtos);
    void registrarSaida(Caixa caixa, Compra novaCompra, ArrayList<Funcionario> funcionarios,
                        ArrayList<Produto> produtos);
}
