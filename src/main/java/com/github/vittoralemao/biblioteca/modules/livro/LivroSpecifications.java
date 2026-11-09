package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.share.BuscaTexto;
import org.springframework.data.jpa.domain.Specification;

public final class LivroSpecifications {

    private LivroSpecifications(){
    }

    public static Specification<Livro> tituloContem(String titulo){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("titulo")), BuscaTexto.contem(titulo), BuscaTexto.ESCAPE);
    }

    public static Specification<Livro> autorContem(String autor){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("autor").get("nome")), BuscaTexto.contem(autor), BuscaTexto.ESCAPE);
    }

    public static Specification<Livro> categoriaContem(String categoria){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("categoria").get("nome")), BuscaTexto.contem(categoria), BuscaTexto.ESCAPE);
    }

}
