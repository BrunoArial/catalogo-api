package com.bruno.catalogo;

public class MensagemResponse {
    private final String mensagem;
    private final String nome;

    public MensagemResponse (String mensagem, String nome) {
        this.mensagem = mensagem;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getMensagem() {
        return mensagem;
    }

}
