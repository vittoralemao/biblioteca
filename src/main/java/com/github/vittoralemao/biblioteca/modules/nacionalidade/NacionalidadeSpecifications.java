package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class NacionalidadeSpecifications {

    private NacionalidadeSpecifications() {
    }

    public static Specification<Nacionalidade> nomeContem(String nome) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase(Locale.ROOT) + "%");
    }

    public static Specification<Nacionalidade> isoContem(String iso) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("iso")), "%" + iso.toLowerCase(Locale.ROOT) + "%");
    }
}
