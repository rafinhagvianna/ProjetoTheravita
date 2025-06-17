package Exceptions;

public class ProdutoException extends Exception {

    public ProdutoException() {
        super("Erro relacionado ao Produto.");
    }

    public ProdutoException(String message) {
        super(message);
    }

    public ProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}