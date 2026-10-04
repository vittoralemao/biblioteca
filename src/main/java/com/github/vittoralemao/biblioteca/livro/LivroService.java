package com.github.vittoralemao.biblioteca.livro;

import com.github.vittoralemao.biblioteca.autor.Autor;
import com.github.vittoralemao.biblioteca.autor.AutorNaoEncontradoException;
import com.github.vittoralemao.biblioteca.autor.AutorRepository;
import com.github.vittoralemao.biblioteca.categoria.Categoria;
import com.github.vittoralemao.biblioteca.categoria.CategoriaNaoEncontradaException;
import com.github.vittoralemao.biblioteca.categoria.CategoriaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

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
    public LivroResponseDTO cadastrar(LivroRequestDTO dto){
        Livro livro = toEntity(dto);

        if (livro.getIsbn() != null){
            if (livroRepository.existsByIsbn(livro.getIsbn())) {
                throw new LivroDuplicadoException("Livro já existente!");
            }
        } else {
            if (livroRepository.existsByTituloAndAutor(livro.getTitulo(), livro.getAutor())){
                throw new LivroDuplicadoException("Livro já existente!");
            }
        }

        Livro livroSalvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(livroSalvo);
    }

    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(UUID id){
        Livro livro = livroRepository.
                findById(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        return LivroResponseDTO.fromEntity(livro);
    }

    @Transactional
    public LivroResponseDTO atualizar(UUID id, LivroRequestDTO dto){
        Livro livro = livroRepository.
                findById(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        Autor autor = autorRepository.
                findById(dto.autorId()).
                orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + dto.autorId()));
        Categoria categoria = categoriaRepository.
                findById(dto.categoriaId()).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + dto.categoriaId()));

        livro.setTitulo(dto.titulo());
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());

        if (livro.getIsbn() != null){
            if (livroRepository.existsByIsbnAndIdNot(livro.getIsbn(), livro.getId())) {
                throw new LivroDuplicadoException("Livro com informações iguais encontrado! ");
            }
        } else {
            if (livroRepository.existsByTituloAndAutorAndIdNot(livro.getTitulo(), livro.getAutor(), livro.getId())){
                throw new LivroDuplicadoException("Livro com informações iguais encontrado! ");
            }
        }

        Livro livroSalvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(livroSalvo);
    }

    @Transactional
    public void excluir(UUID id){
        Livro livro = livroRepository.
                findById(id).
                orElseThrow(() -> new LivroNaoEncontradoException(LIVRO_NAO_ENCONTRADO + id));
        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listar(@Nullable String titulo, @Nullable String autor, @Nullable String categoria){
        List<Livro> livros;

        if (titulo != null && autor != null && categoria != null) {
            livros = livroRepository.findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(titulo, autor, categoria);
        } else if (autor != null && categoria != null){
            livros = livroRepository.findByAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(autor, categoria);
        } else if (titulo != null && categoria != null) {
            livros = livroRepository.findByTituloContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(titulo, categoria);
        } else if (titulo != null && autor != null) {
            livros = livroRepository.findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCase(titulo, autor);
        } else if (categoria != null) {
            livros = livroRepository.findByCategoria_NomeContainingIgnoreCase(categoria);
        } else if (autor != null) {
            livros = livroRepository.findByAutor_NomeContainingIgnoreCase(autor);
        } else if (titulo != null) {
            livros = livroRepository.findByTituloContainingIgnoreCase(titulo);
        } else {
            livros = livroRepository.findAll();
        }

        return livros.stream()
                .map(LivroResponseDTO::fromEntity)
                .toList();
    }

    private Livro toEntity(LivroRequestDTO dto){
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
