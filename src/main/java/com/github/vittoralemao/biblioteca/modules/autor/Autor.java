package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.modules.livro.Livro;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.share.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Autor extends EntidadeAuditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nacionalidade_id", nullable = false)
    private Nacionalidade nacionalidade;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Livro> livros = new ArrayList<>();
}
