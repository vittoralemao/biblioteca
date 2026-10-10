package com.github.vittoralemao.biblioteca.modules.categoria;

import com.github.vittoralemao.biblioteca.modules.livro.LivroRepository;
import org.jspecify.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;

    private static final String CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada: ";
    private static final String CATEGORIA_POSSUI_LIVRO_VINCULADO = "Categoria possui livro(s) vinculado(s) e não pode ser excluído: ";



    @Transactional
    public CategoriaResponseDTO cadastrar(CategoriaRequestDTO dto){
        Categoria categoria = toEntity(dto);

        if (categoriaRepository.existsByNome(categoria.getNome())){
            throw new CategoriaDuplicadaException("Categoria já existente! ");
        }

        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoriaSalva);
    }

    @Transactional
    public CategoriaResponseDTO atualizar(UUID id, CategoriaRequestDTO dto){
        Categoria categoria = categoriaRepository.
                findById(id).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + id));

        categoria.setNome(dto.nome());
        if (categoriaRepository.existsByNomeAndIdNot(categoria.getNome(), categoria.getId())){
            throw new CategoriaDuplicadaException("Categoria com informações iguais encontrada! ");
        }

        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoriaSalva);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(UUID id){
        Categoria categoria = categoriaRepository.
                findById(id).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + id));
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar(@Nullable String nome){
        List<Categoria> categorias;

        if (nome != null) {
            categorias = categoriaRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            categorias = categoriaRepository.findAll();
        }

        return categorias.stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void excluir(UUID id){
        Categoria categoria = categoriaRepository.
                findById(id).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + id));

        if(livroRepository.existsByCategoria((categoria))){
            throw new CategoriaComLivroVinculadoException(CATEGORIA_POSSUI_LIVRO_VINCULADO + id);
        }

        categoriaRepository.delete(categoria);
    }


    private Categoria toEntity(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        return categoria;
    }

}
