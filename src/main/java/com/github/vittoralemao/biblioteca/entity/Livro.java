package com.github.vittoralemao.biblioteca.entity;

import com.github.vittoralemao.biblioteca.enums.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "categoria", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;
}
