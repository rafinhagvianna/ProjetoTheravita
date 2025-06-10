package Interfaces;

import Classes.Produto;
import java.util.ArrayList;

public interface IntProduto {
     void menu();
     void cadastraProduto(ArrayList<Produto> produtos);
     void listarProdutos(ArrayList<Produto> produtos);
     void atualizarProduto(ArrayList<Produto> produtos);
     void diponibilidadeProduto(ArrayList<Produto> produtos);
}
