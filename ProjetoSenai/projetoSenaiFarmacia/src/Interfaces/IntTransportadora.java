package Interfaces;

import Classes.Transportadora;

import java.util.ArrayList;
import java.util.Scanner;

public interface IntTransportadora {
    void menu();
    void cadastrarTransportadora(Scanner scanner, ArrayList<Transportadora> transportadoras);

}
