package com.bruno.catalogo;
import java.util.List;

public class ErroResponse {
    private final String mensagem;
    private final List<String> erros;

    public ErroResponse(String mensagem, List<String> erros) {
        this.mensagem = mensagem;
        this.erros = erros;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getErros() {
        return erros;
    }
}
