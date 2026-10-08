package com.bruno.catalogo;
import java.math.BigDecimal;

public class ProdutoResponse {
    private final Long id;
    private final String nome;
    private final BigDecimal preco;

    public ProdutoResponse(Long id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
