package com.github.vittoralemao.biblioteca.service;

import com.github.vittoralemao.biblioteca.dto.AutorRequestDTO;
import com.github.vittoralemao.biblioteca.dto.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorResponseDTO cadastrar(AutorRequestDTO dto) {
        Autor autor = toEntity(dto);
        Autor autorSalvo = autorRepository.save(autor);
        return AutorResponseDTO.fromEntity(autorSalvo);
    }

    private Autor toEntity(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setDataNascimento(dto.dataNascimento());
        autor.setNacionalidade(dto.nacionalidade());
        return autor;
    }
}
