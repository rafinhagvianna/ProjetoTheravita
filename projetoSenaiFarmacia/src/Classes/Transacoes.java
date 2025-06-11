package Classes;
import java.util.ArrayList;

import Enums.Status;

import java.time.LocalDate;

public abstract class Transacoes {
    private String id;
    private ArrayList<Itens> produtos;
    private LocalDate data;
    private Funcionario funcionario;
    private Status status;
    private double valor;
    private static int proxId = 1;

    public Transacoes() {
        this.id = "TX"+proxId;
        proxId++;
        this.data = LocalDate.now();
    }

    public Transacoes(ArrayList<Itens> produtos, LocalDate data, Funcionario funcionario, Status status, double valor) {
        this.id = "TX"+proxId;
        this.produtos = produtos;
        this.data = data;
        this.funcionario = funcionario;
        this.status = status;
        this.valor = valor;
        proxId++;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Itens> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Itens> produtos) {
        this.produtos = produtos;
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

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public double getValor(){
        return valor;
    }

    public void setValor(double valor){
        this.valor = valor;
    }

    public boolean verificaEstoque(int qtd){
        return qtd >= 0 ?  true :  false;
    }

    public abstract double calculaTotal();

    @Override
    public String toString() {
        String result;
        result ="Transacoes{" +
                "\n  id='" + id + '\'' +
                "\n  produtos= [";
        for (Itens item : produtos) {
            result +="\n    - "+ item.getQuantidade()+" "+ item.getProduto().getDescricao();
        }
        result +="\n  ]\n  data=" + data +
                "\n  status=" + status +
                "\n  valor=" + valor +
                "\n}";
        return result;
    }
}