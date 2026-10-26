package com.github.vittoralemao.biblioteca.modules.autor;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class AutorSpecifications {

    private AutorSpecifications(){
    }

    public static Specification<Autor> nomeContem(String nome){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Autor> nacionalidadeContem(String nacionalidade){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nacionalidade").get("nome")), "%" + nacionalidade.toLowerCase(Locale.ROOT) + "%");
    }
}
