package com.github.vittoralemao.biblioteca.nacionalidade;

import com.github.vittoralemao.biblioteca.autor.AutorRepository;
import org.jspecify.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NacionalidadeService {

    private final NacionalidadeRepository nacionalidadeRepository;
    private final AutorRepository autorRepository;

    private static final String NACIONALIDADE_NAO_ENCONTRADA = "Nacionalidade não encontrada: ";
    private static final String NACIONALIDADE_POSSUI_AUTOR_VINCULADO = "Nacionalidade possui autor(s) vinculado(s) e não pode ser excluída: ";

    @Transactional
    public NacionalidadeResponseDTO cadastrar (NacionalidadeRequestDTO dto){
        Nacionalidade nacionalidade = toEntity(dto);

        if(nacionalidadeRepository.existsByNomeAndIso(nacionalidade.getNome(), nacionalidade.getIso())) {
            throw new NacionalidadeDuplicadaException("Nacionalidade já existente! ");
        }

        Nacionalidade nacionalidadeSalva = nacionalidadeRepository.save(nacionalidade);
        return NacionalidadeResponseDTO.fromEntity(nacionalidadeSalva);
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public NacionalidadeResponseDTO buscarPorId(UUID id){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));
        return NacionalidadeResponseDTO.fromEntity(nacionalidade);
    }

    @Transactional(readOnly = true)
    public List<NacionalidadeResponseDTO> listar(@Nullable String nome, @Nullable String iso) {

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

    @Transactional
    public void excluir(UUID id){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));

        if(autorRepository.existsByNacionalidade(nacionalidade)) {
            throw new NacionalidadeComAutorVinculadoException(NACIONALIDADE_POSSUI_AUTOR_VINCULADO + id);
        }

        nacionalidadeRepository.delete(nacionalidade);
    }

    private Nacionalidade toEntity(NacionalidadeRequestDTO dto){
        Nacionalidade nacionalide = new Nacionalidade();
        nacionalide.setIso(dto.iso());
        nacionalide.setNome(dto.nome());
        return nacionalide;
    }
}
