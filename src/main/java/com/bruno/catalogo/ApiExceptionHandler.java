package com.bruno.catalogo;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse tratarProdutoNaoEncontrado(ProdutoNaoEncontradoException exception) {
        return new ErroResponse(exception.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarValidacao(MethodArgumentNotValidException exception) {
        List<String> erros = new ArrayList<>();

        for (FieldError erro : exception.getBindingResult().getFieldErrors()) {
            erros.add(erro.getDefaultMessage());
        }

        return new ErroResponse("Dados inválidos.", erros);
    }
}
