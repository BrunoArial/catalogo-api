package com.bruno.catalogo;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    public List<Produto> listar() {
        Produto produto1 = new Produto(1L, "Teclado", new BigDecimal("150.00"));
        Produto produto2 = new Produto(2L, "Mouse", new BigDecimal("80.00"));

        return List.of(produto1, produto2);
    }
}
