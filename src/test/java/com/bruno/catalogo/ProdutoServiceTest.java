package com.bruno.catalogo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

public class ProdutoServiceTest {
    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;

    @BeforeEach
    void preparar() {
        produtoRepository = mock(ProdutoRepository.class);
        produtoService = new ProdutoService(produtoRepository);
    }

    @Test
    void deveBuscarProdutoExistente() {
        Produto produto = new Produto(1L, "Monitor", new BigDecimal("950.00"));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarPorId(1L);

        assertSame(produto, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorId(99L));
    }

    @Test
    void deveAtualizarProdutoMantendoId() {
        Produto produtoExistente = new Produto(1L, "Monitor", new BigDecimal("950.00"));

        ProdutoRequest requestAtualizado = new ProdutoRequest();
        requestAtualizado.setNome("Monitor Atualizado");
        requestAtualizado.setPreco(new BigDecimal("1000.00"));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(produtoExistente)).thenReturn(produtoExistente);

        Produto produtoAtualizado = produtoService.atualizar(1L, requestAtualizado);

        assertEquals(1L, produtoAtualizado.getId());
        assertEquals("Monitor Atualizado", produtoAtualizado.getNome());
        assertEquals(new BigDecimal("1000.00"), produtoAtualizado.getPreco());

        verify(produtoRepository).save(produtoExistente);
    }

    @Test
    void naoDeveSalvarAoAtualizarProdutoInexistente() {
        ProdutoRequest produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Monitor Atualizado");
        produtoRequest.setPreco(new BigDecimal("1000.00"));

        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.atualizar(99L, produtoRequest));

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void deveRemoverProdutoExistente() {
        Produto produto = new Produto(1L, "Monitor", new BigDecimal("950.00"));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.remover(1L);

        verify(produtoRepository).delete(produto);
    }

    @Test
    void naoDeveExcluirProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.remover(99L));

        verify(produtoRepository, never()).delete(any(Produto.class));
    }
}

