package Interfaces;

import Classes.Caixa;

import java.util.Scanner;

public interface IntGestao {
    void apresentarMenuGestao(Scanner scanner , Caixa caixa);
    void consultarNegocios();
    void atualizarStatus(Scanner scanner, Caixa caixa);
}
