package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.share.BuscaTexto;
import org.springframework.data.jpa.domain.Specification;

public final class AutorSpecifications {

    private AutorSpecifications(){
    }

    public static Specification<Autor> nomeContem(String nome){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), BuscaTexto.contem(nome), BuscaTexto.ESCAPE);
    }

    public static Specification<Autor> nacionalidadeContem(String nacionalidade){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nacionalidade").get("nome")), BuscaTexto.contem(nacionalidade), BuscaTexto.ESCAPE);
    }
}
