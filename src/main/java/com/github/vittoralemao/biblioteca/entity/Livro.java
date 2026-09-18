package com.github.vittoralemao.biblioteca.entity;

import com.github.vittoralemao.biblioteca.enums.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "categoria", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;
}
