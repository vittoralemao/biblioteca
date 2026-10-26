package com.github.vittoralemao.biblioteca.modules.livro;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class LivroSpecifications {

    private LivroSpecifications(){
    }

    public static Specification<Livro> tituloContem(String titulo){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Livro> autorContem(String autor){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("autor").get("nome")), "%" + autor.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Livro> categoriaContem(String categoria){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("categoria").get("nome")), "%" + categoria.toLowerCase(Locale.ROOT) + "%");
    }

}
