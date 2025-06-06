import java.util.ArrayList;

import Enums.Status;

import java.time.LocalDate;

public abstract class Transacoes {
    private String id;
    private ArrayList<Produto> produtos;
    private double total;
    private LocalDate data;
    private Funcionario funcionario;
    private ArrayList<Status> status;
    protected static int proxId = 1;
    private double valor;

    public Transacoes() {
        this.id = "TX"+proxId;
        proxId++;
    }

    public Transacoes(String id, ArrayList<Produto> produtos, double total, LocalDate data, Funcionario funcionario, ArrayList<Status> status, double valor) {
        this.id = "TX"+proxId;
        this.produtos = produtos;
        this.total = total;
        this.data = data;
        this.funcionario = funcionario;
        this.status = status;
        this.valor = valor;
        proxId++;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public double getTotal() {
        return total;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public ArrayList<Status> getStatus(){
        return status;
    }

    public void setStatus(ArrayList<Status> status){
        this.status = status;
    }

    public double getValor(){
        return valor;
    }

    public boolean verificaEstoque(int qtd){
        return qtd >= 0 ?  true :  false;
    }
}