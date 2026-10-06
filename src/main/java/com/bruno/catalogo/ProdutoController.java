package com.bruno.catalogo;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController 
public class ProdutoController {
    
    private final ProdutoService produtoService;
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @GetMapping("/produtos/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping("/produtos")
    @ResponseStatus(HttpStatus.CREATED)
    public Produto cadastrar(@RequestBody ProdutoRequest request) {
        return produtoService.cadastrar(request);
    }
}
