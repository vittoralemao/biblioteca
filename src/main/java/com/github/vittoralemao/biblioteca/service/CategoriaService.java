package com.github.vittoralemao.biblioteca.service;

import com.github.vittoralemao.biblioteca.dto.CategoriaRequestDTO;
import com.github.vittoralemao.biblioteca.dto.CategoriaResponseDTO;
import com.github.vittoralemao.biblioteca.entity.Categoria;
import com.github.vittoralemao.biblioteca.exception.CategoriaDuplicadaException;
import com.github.vittoralemao.biblioteca.exception.CategoriaNaoEncontradaException;
import com.github.vittoralemao.biblioteca.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    private static final String CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada: ";

    public CategoriaResponseDTO cadastrar(CategoriaRequestDTO dto){
        Categoria categoria = toEntity(dto);

        if (categoriaRepository.existsByNome(categoria.getNome())){
            throw new CategoriaDuplicadaException("Categoria já existente! ");
        }

        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(categoriaSalva);
    }

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

    public CategoriaResponseDTO buscarPorId(UUID id){
        Categoria categoria = categoriaRepository.
                findById(id).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + id));
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    public List<CategoriaResponseDTO> listar(String nome){
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

    public void excluir(UUID id){
        Categoria categoria = categoriaRepository.
                findById(id).
                orElseThrow(() -> new CategoriaNaoEncontradaException(CATEGORIA_NAO_ENCONTRADA + id));
        categoriaRepository.delete(categoria);
    }


    private Categoria toEntity(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        return categoria;
    }

}
