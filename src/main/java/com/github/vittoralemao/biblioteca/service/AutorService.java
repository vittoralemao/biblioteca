package com.github.vittoralemao.biblioteca.service;

import com.github.vittoralemao.biblioteca.dto.AutorRequestDTO;
import com.github.vittoralemao.biblioteca.dto.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.enums.Nacionalidade;
import com.github.vittoralemao.biblioteca.exception.AutorNaoEncontradoException;
import com.github.vittoralemao.biblioteca.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private static final String AUTOR_NAO_ENCONTRADO = "Autor não encontrado: ";

    public AutorResponseDTO cadastrar(AutorRequestDTO dto) {
        Autor autor = toEntity(dto);
        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }



    public AutorResponseDTO buscarPorId(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));
        return AutorResponseDTO.fromEntity(autor);
    }

    public AutorResponseDTO atualizar(UUID id, AutorRequestDTO dto) {

        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));

        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(dto.nacionalidade());

        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }

    public void excluir(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));

        autorRepository.delete(autor);
    }

    public List<AutorResponseDTO> listar(String nome, String nacionalidade) {

        Nacionalidade nacionalidadeEnum = nacionalidade != null
                ? Nacionalidade.valueOf(nacionalidade.toUpperCase())
                : null;

        List<Autor> autores;

        if (nome != null && nacionalidadeEnum != null) {
            autores = autorRepository.findByNomeContainingIgnoreCaseAndNacionalidade(nome, nacionalidadeEnum);
        } else if (nome != null){
            autores = autorRepository.findByNomeContainingIgnoreCase(nome);
        } else if (nacionalidade != null) {
            autores = autorRepository.findByNacionalidade(nacionalidadeEnum);
        } else {
            autores = autorRepository.findAll();
        }

        return autores.stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    private Autor toEntity(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(dto.nacionalidade());
        return autor;
    }


}
