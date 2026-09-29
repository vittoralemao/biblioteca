package com.github.vittoralemao.biblioteca.service;

import com.github.vittoralemao.biblioteca.dto.NacionalidadeRequestDTO;
import com.github.vittoralemao.biblioteca.dto.NacionalidadeResponseDTO;
import com.github.vittoralemao.biblioteca.entity.Nacionalidade;
import com.github.vittoralemao.biblioteca.exception.NacionalidadeDuplicadaException;
import com.github.vittoralemao.biblioteca.exception.NacionalidadeNaoEncontradaException;
import com.github.vittoralemao.biblioteca.repository.NacionalidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NacionalidadeService {

    private final NacionalidadeRepository nacionalidadeRepository;

    private static final String NACIONALIDADE_NAO_ENCONTRADA = "Nacionalidade não encontrada: ";

    public NacionalidadeResponseDTO cadastrar (NacionalidadeRequestDTO dto){
        Nacionalidade nacionalidade = toEntity(dto);

        if(nacionalidadeRepository.existsByNomeAndIso(nacionalidade.getNome(), nacionalidade.getIso())) {
            throw new NacionalidadeDuplicadaException("Nacionalidade já existente! ");
        }

        Nacionalidade nacionalidadeSalva = nacionalidadeRepository.save(nacionalidade);
        return NacionalidadeResponseDTO.fromEntity(nacionalidadeSalva);
    }

    public NacionalidadeResponseDTO atualizar(UUID id, NacionalidadeRequestDTO dto){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));

        nacionalidade.setNome(dto.nome());
        nacionalidade.setIso(dto.iso());

        if(nacionalidadeRepository.existsByNomeAndIsoAndIdNot(nacionalidade.getNome(), nacionalidade.getIso(), nacionalidade.getId())) {
            throw new NacionalidadeDuplicadaException("Nacionalidade com informações iguais encontrada! ");
        }

        Nacionalidade nacionalidadeSalva = nacionalidadeRepository.save(nacionalidade);
        return NacionalidadeResponseDTO.fromEntity(nacionalidadeSalva);
    }

    public NacionalidadeResponseDTO buscarPorId(UUID id){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));
        return NacionalidadeResponseDTO.fromEntity(nacionalidade);
    }

    public List<NacionalidadeResponseDTO> listar(String nome, String iso) {

        List<Nacionalidade> nacionalidades;

        if (nome != null) {
            nacionalidades = nacionalidadeRepository.findByNomeContainingIgnoreCase(nome);
        } else if (iso != null){
            nacionalidades = nacionalidadeRepository.findByIsoContainingIgnoreCase(iso);
        } else {
            nacionalidades = nacionalidadeRepository.findAll();
        }

        return nacionalidades.stream()
                .map(NacionalidadeResponseDTO::fromEntity)
                .toList();
    }

    public void excluir(UUID id){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));

        nacionalidadeRepository.delete(nacionalidade);
    }

    private Nacionalidade toEntity(NacionalidadeRequestDTO dto){
        Nacionalidade nacionalide = new Nacionalidade();
        nacionalide.setIso(dto.iso());
        nacionalide.setNome(dto.nome());
        return nacionalide;
    }
}
