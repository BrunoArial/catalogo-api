package com.bruno.catalogo;
import java.math.BigDecimal;

public class ProdutoRequest {
    private String nome;
    private BigDecimal preco;

    public String getNome() {
        return this.nome;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
