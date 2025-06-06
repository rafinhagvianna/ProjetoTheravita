public class Itens {
    
    private int quantidade;
    private Produto produto;

    public Itens(int quantidade, Produto produto){
        this.quantidade = quantidade;
        this.produto = produto;
    }
    
    public int getQuantidade(){
        return quantidade;
    }
    public Produto getProduto(){
        return produto;
    }
    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    public void setProduto(Produto produto){
        this.produto = produto;
    }

    public double valorVenda(){
        return quantidade * produto.getValorVenda();
    }
    
    public double valorCompra(){
        return quantidade * produto.getValorCompra();
    }
}
