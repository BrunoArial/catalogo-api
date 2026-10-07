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
        return produtoRepository.listar();
    }

    public Produto buscarPorId(Long id) { 
        for (Produto produto : listar()) {
            if (produto.getId().equals(id)) {
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException("Produto não encontrado.");
    }

    public Produto cadastrar(ProdutoRequest request) {
        return produtoRepository.cadastrar(request.getNome(), request.getPreco());
    }

    public Produto atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarPorId(id);
        produto.atualizarDados(request.getNome(), request.getPreco());
        return produto;
    }

    public void remover(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.remover(produto);
    }
}
