package com.bruno.catalogo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController 
public class MensagemController {

    @GetMapping("/mensagem")
    public MensagemResponse mensagem(@RequestParam(defaultValue = "visitante") String nome) {
        String texto = "Olá, " + nome + "! Bem-vindo ao catálogo.";
        
        return new MensagemResponse(texto, nome);
    }

}
