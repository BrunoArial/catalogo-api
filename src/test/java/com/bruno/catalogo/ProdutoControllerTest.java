package com.bruno.catalogo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void deveBuscarProdutoPorId() throws Exception{
        Produto produto = new Produto(1L, "Monitor", new BigDecimal("950.00"));
        when(produtoService.buscarPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Monitor"))
            .andExpect(jsonPath("$.preco").value(950.00));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExistir() throws Exception {
        when(produtoService.buscarPorId(99L)).thenThrow(new ProdutoNaoEncontradoException("Produto não encontrado."));

        mockMvc.perform(get("/produtos/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem").value("Produto não encontrado."))
            .andExpect(jsonPath("$.erros").isEmpty());
    }

    @Test
    void deveRejeitarCadastroInvalido() throws Exception {
        String json = """
    {
        "nome": "   ",
        "preco": -50.00
    }
    """;

        mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("Dados inválidos."))
            .andExpect(jsonPath("$.erros", containsInAnyOrder("O nome é obrigatório.","O preço deve ser um valor maior que zero.")));
        
        verifyNoInteractions(produtoService);
    }

    @Test
    void deveRejeitarCadastroSemPreco() throws Exception {
        String json = """
    {
        "nome": "Monitor"
    }
    """;

        mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("Dados inválidos."))
            .andExpect(jsonPath("$.erros", containsInAnyOrder("O preço é obrigatório.")));

        verifyNoInteractions(produtoService);
    }

    @Test 
    void deveRejeitarAtualizacaoInvalida() throws Exception {
        String json = """
    {
        "nome": "",
        "preco": 0
    }
    """;

        mockMvc.perform(put("/produtos/1").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("Dados inválidos."))
            .andExpect(jsonPath("$.erros", containsInAnyOrder("O nome é obrigatório.","O preço deve ser um valor maior que zero.")));
        
        verifyNoInteractions(produtoService);
    }

    @Test
    void deveCadastrarProdutoValido() throws Exception {
        Produto produto = new Produto(1L, "Monitor", new BigDecimal("950.00"));
        when(produtoService.cadastrar(any(ProdutoRequest.class))).thenReturn(produto);

        String json = """
    {
        "nome": "Monitor",
        "preco": 950.00
    }
    """;

        mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Monitor"))
            .andExpect(jsonPath("$.preco").value(950.00));

        ArgumentCaptor<ProdutoRequest> captor = ArgumentCaptor.forClass(ProdutoRequest.class);
        
        verify(produtoService).cadastrar(captor.capture());

        ProdutoRequest requestRecebido = captor.getValue();

        assertEquals("Monitor", requestRecebido.getNome());
        assertEquals(new BigDecimal("950.00"), requestRecebido.getPreco());
    }
}