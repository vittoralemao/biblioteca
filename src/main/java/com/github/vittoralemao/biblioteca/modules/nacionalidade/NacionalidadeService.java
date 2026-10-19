package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
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

        if(nacionalidadeRepository.existsByNome(nacionalidade.getNome())) {
            throw new NacionalidadeDuplicadaException("Já existe nacionalidade com o nome: " + dto.nome());
        }

        if (nacionalidadeRepository.existsByIso(nacionalidade.getIso())) {
            throw new NacionalidadeDuplicadaException("Já existe nacionalidade com o ISO: " + dto.iso());
        }

        Nacionalidade nacionalidadeSalva = nacionalidadeRepository.save(nacionalidade);
        return NacionalidadeResponseDTO.fromEntity(nacionalidadeSalva);
    }

    @Transactional
    public NacionalidadeResponseDTO atualizar(UUID id, NacionalidadeRequestDTO dto){
        Nacionalidade nacionalidade = nacionalidadeRepository.
                findById(id)
                .orElseThrow(() -> new NacionalidadeNaoEncontradaException(NACIONALIDADE_NAO_ENCONTRADA + id));

        if(nacionalidadeRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new NacionalidadeDuplicadaException("Já existe nacionalidade com o nome: " + dto.nome());
        }

        if(nacionalidadeRepository.existsByIsoAndIdNot(dto.iso(), id)) {
            throw new NacionalidadeDuplicadaException("Já existe nacionalidade com o ISO " + dto.iso());
        }

        nacionalidade.setNome(dto.nome());
        nacionalidade.setIso(dto.iso());

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
