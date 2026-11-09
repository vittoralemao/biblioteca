package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import com.github.vittoralemao.biblioteca.share.BuscaTexto;
import org.springframework.data.jpa.domain.Specification;

public final class NacionalidadeSpecifications {

    private NacionalidadeSpecifications() {
    }

    public static Specification<Nacionalidade> nomeContem(String nome) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), BuscaTexto.contem(nome), BuscaTexto.ESCAPE);
    }

    public static Specification<Nacionalidade> isoContem(String iso) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("iso")), BuscaTexto.contem(iso), BuscaTexto.ESCAPE);
    }
}
