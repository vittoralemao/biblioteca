package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import com.github.vittoralemao.biblioteca.share.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livro extends EntidadeAuditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "isbn", unique = true, length = 20)
    @Nullable
    private String isbn;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;
}
