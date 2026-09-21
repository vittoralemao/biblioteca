package com.github.vittoralemao.biblioteca.controller;

import com.github.vittoralemao.biblioteca.dto.AutorRequestDTO;
import com.github.vittoralemao.biblioteca.dto.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorResponseDTO> cadastrar(@RequestBody @Valid AutorRequestDTO dto) {

        AutorResponseDTO autorCriado = autorService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorCriado.id())
                .toUri();

        return ResponseEntity.created(location).body(autorCriado);

    }

}
