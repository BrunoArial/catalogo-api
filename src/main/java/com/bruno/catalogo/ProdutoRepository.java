package com.bruno.catalogo;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

@Repository
public class ProdutoRepository {
    private final List<Produto> produtos = new ArrayList<>();
    private Long proximoId = 3L;

    public ProdutoRepository() {
        Produto produto1 = new Produto(1L, "Teclado", new BigDecimal("150.00"));
        Produto produto2 = new Produto(2L, "Mouse", new BigDecimal("80.00"));
        produtos.add(produto1);
        produtos.add(produto2);
    }

    public List<Produto> listar() {
        return List.copyOf(produtos);
    }

    public Produto cadastrar(String nome, BigDecimal preco) {
        Produto novoProduto = new Produto(proximoId, nome, preco);
        produtos.add(novoProduto);
        proximoId++;
        return novoProduto;
    }

    public void remover(Produto produto) {
        produtos.remove(produto);
    }
}
