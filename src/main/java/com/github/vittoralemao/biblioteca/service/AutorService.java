package com.github.vittoralemao.biblioteca.service;

import com.github.vittoralemao.biblioteca.dto.AutorRequestDTO;
import com.github.vittoralemao.biblioteca.dto.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.entity.Nacionalidade;
import com.github.vittoralemao.biblioteca.exception.AutorDuplicadoException;
import com.github.vittoralemao.biblioteca.exception.AutorNaoEncontradoException;
import com.github.vittoralemao.biblioteca.exception.NacionalidadeNaoEncontradaException;
import com.github.vittoralemao.biblioteca.repository.AutorRepository;
import com.github.vittoralemao.biblioteca.repository.NacionalidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final NacionalidadeRepository nacionalidadeRepository;

    private static final String AUTOR_NAO_ENCONTRADO = "Autor não encontrado: ";
    private static final String NACIONALIDADE_NAO_ENCONTRADA = "Nacionalidade não encontrada: ";

    public AutorResponseDTO cadastrar(AutorRequestDTO dto) {
        Autor autor = toEntity(dto);

        if (autorRepository.existsByNomeAndDataNascimentoAndNacionalidade(autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade())) {
            throw new AutorDuplicadoException("Autor já existente!");
        }

        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }

    public AutorResponseDTO buscarPorId(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));
        return AutorResponseDTO.fromEntity(autor);
    }

    public AutorResponseDTO atualizar(UUID id, AutorRequestDTO dto) {

        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));
        Nacionalidade nacionalidade = nacionalidadeRepository.findById(
                dto.nacionalidadeId())
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + dto.nacionalidadeId()));

        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(nacionalidade);

        if (autorRepository.existsByNomeAndDataNascimentoAndNacionalidadeAndIdNot(autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade(), autor.getId())) {
            throw new AutorDuplicadoException("Autor com informações iguais encontrado! ");
        }

        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }

    public void excluir(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));

        autorRepository.delete(autor);
    }

    public List<AutorResponseDTO> listar(String nome, String nacionalidade) {

        List<Autor> autores;

        if (nome != null && nacionalidade != null) {
            autores = autorRepository.findByNomeContainingIgnoreCaseAndNacionalidade_NomeContainingIgnoreCase(nome, nacionalidade);
        } else if (nome != null){
            autores = autorRepository.findByNomeContainingIgnoreCase(nome);
        } else if (nacionalidade != null) {
            autores = autorRepository.findByNacionalidade_NomeContainingIgnoreCase(nacionalidade);
        } else {
            autores = autorRepository.findAll();
        }

        return autores.stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    private Autor toEntity(AutorRequestDTO dto) {

        Nacionalidade nacionalidade  = nacionalidadeRepository.findById(
                dto.nacionalidadeId())
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + dto.nacionalidadeId()));

        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(nacionalidade);
        return autor;
    }


}
