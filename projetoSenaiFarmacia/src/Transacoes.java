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
    private static int proxId = 1;

    public Transacoes() {
        this.id = "TX"+proxId;
    }

    public Transacoes(String id, ArrayList<Produto> produtos, double total, LocalDate data, Funcionario funcionario, ArrayList<Status> status) {
        this.id = id;
        this.produtos = produtos;
        this.total = total;
        this.data = data;
        this.funcionario = funcionario;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setTotal(double total) {
        this.total = total;
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

    public boolean verificaEstoque(int qtd){
        return qtd >= 0 ?  true :  false;
    }
}