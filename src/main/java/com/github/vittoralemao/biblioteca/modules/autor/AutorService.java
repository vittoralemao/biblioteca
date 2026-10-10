package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.modules.livro.LivroRepository;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeNaoEncontradaException;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final NacionalidadeRepository nacionalidadeRepository;
    private final LivroRepository livroRepository;

    private static final String AUTOR_NAO_ENCONTRADO = "Autor não encontrado: ";
    private static final String NACIONALIDADE_NAO_ENCONTRADA = "Nacionalidade não encontrada: ";
    private static final String AUTOR_POSSUI_LIVRO_VINCULADO = "Autor possui livro(s) vinculado(s) e não pode ser excluído: ";


    @Transactional
    public AutorResponseDTO cadastrar(AutorRequestDTO dto) {
        Autor autor = toEntity(dto);

        if (autorRepository.existsByNomeAndDataNascimentoAndNacionalidade(autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade())) {
            throw new AutorDuplicadoException("Autor já existente!");
        }

        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));
        return AutorResponseDTO.fromEntity(autor);
    }

    @Transactional
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
    @Transactional
    public void excluir(UUID id) {
        Autor autor = autorRepository.findById(id).orElseThrow(() -> new AutorNaoEncontradoException(AUTOR_NAO_ENCONTRADO + id));

        if (livroRepository.existsByAutor(autor)) {
            throw new AutorPossuiLivroVinculadoException(AUTOR_POSSUI_LIVRO_VINCULADO + id);
        }

        autorRepository.delete(autor);
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listar(@Nullable String nome, @Nullable String nacionalidade) {

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
