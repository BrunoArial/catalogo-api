package com.bruno.catalogo;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import java.util.ArrayList;

@RestController 
public class ProdutoController {
    
    private final ProdutoService produtoService;
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    public List<ProdutoResponse> listar() {
        ArrayList<ProdutoResponse> produtosResponse = new ArrayList<>();
        for (Produto produto : produtoService.listar()) {
            produtosResponse.add(new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco()));
        }
        return produtosResponse;
    }

    @GetMapping("/produtos/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }

    @PostMapping("/produtos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastrar(@Valid @RequestBody ProdutoRequest request) {
        Produto produto = produtoService.cadastrar(request);
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }

    @PutMapping("/produtos/{id}")
    public ProdutoResponse atualizar(@PathVariable("id") Long id, @Valid @RequestBody ProdutoRequest request) {
        Produto produto = produtoService.atualizar(id, request);
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco());
    }
    

    @DeleteMapping("/produtos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        produtoService.remover(id);
    }
}
