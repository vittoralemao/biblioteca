package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.autor.AutorNaoEncontradoException;
import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaNaoEncontradaException;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    private static final String LIVRO_NAO_ENCONTRADO = "Livro não encontrado: ";
    private static final String AUTOR_NAO_ENCONTRADO = "Autor não encontrado: ";
    private static final String CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada: ";

    @Transactional
    public LivroResponseDTO cadastrar(LivroRequestDTO dto) {
        Livro livro = toEntity(dto);

        if (livro.getIsbn() != null) {
            if (livroRepository.existsByIsbn(livro.getIsbn())) {
                throw new LivroDuplicadoException("Livro já existente!");
            }
        } else {
            if (livroRepository.existsByTituloAndAutor(livro.getTitulo(), livro.getAutor())) {
                throw new LivroDuplicadoException("Livro já existente!");
            }
        }

        Livro livroSalvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(livroSalvo);
    }

    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(UUID id) {
        Livro livro = livroRepository.
                buscarLivroComAutorECategoria(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        return LivroResponseDTO.fromEntity(livro);
    }

    @Transactional
    public LivroResponseDTO atualizar(UUID id, LivroRequestDTO dto) {
        Livro livro = livroRepository.
                findById(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        Autor autor = autorRepository.
                findById(dto.autorId()).
                orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + dto.autorId()));
        Categoria categoria = categoriaRepository.
                findById(dto.categoriaId()).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + dto.categoriaId()));

        if (dto.isbn() != null) {
            if (livroRepository.existsByIsbnAndIdNot(dto.isbn(), id)) {
                throw new LivroDuplicadoException("Livro com informações iguais encontrado! ");
            }
        } else {
            if (livroRepository.existsByTituloAndAutorAndIdNot(dto.titulo(), autor, id)) {
                throw new LivroDuplicadoException("Livro com informações iguais encontrado! ");
            }
        }

        livro.setTitulo(dto.titulo());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());

        Livro livroSalvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(livroSalvo);
    }

    @Transactional
    public void excluir(UUID id) {
        Livro livro = livroRepository.
                findById(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listar(@Nullable String titulo, @Nullable String autor, @Nullable String categoria) {
        List<Specification<Livro>> filtros = new ArrayList<>();

        if (titulo != null) {
            filtros.add(LivroSpecifications.tituloContem(titulo));
        }

        if (autor != null) {
            filtros.add(LivroSpecifications.autorContem(autor));
        }

        if (categoria != null) {
            filtros.add(LivroSpecifications.categoriaContem(categoria));
        }

        return livroRepository.findAll(Specification.allOf(filtros)).stream()
                .map(LivroResponseDTO::fromEntity)
                .toList();
    }

    private Livro toEntity(LivroRequestDTO dto) {
        Autor autor = autorRepository.findById(dto.autorId()).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + dto.autorId()));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + dto.categoriaId()));

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        return livro;
    }
}
