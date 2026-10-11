package com.bruno.catalogo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) { 
        return produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado."));
    }

    public Produto cadastrar(ProdutoRequest request) {
        Produto produto = new Produto(request.getNome(),request.getPreco());
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarPorId(id);
        produto.atualizarDados(request.getNome(), request.getPreco());
        return produtoRepository.save(produto);
    }

    public void remover(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}
