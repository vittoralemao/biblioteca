package com.github.vittoralemao.biblioteca.controller;

import com.github.vittoralemao.biblioteca.dto.AutorRequestDTO;
import com.github.vittoralemao.biblioteca.dto.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable UUID id) {

        AutorResponseDTO autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String nacionalidade
            ) {

        List<AutorResponseDTO> autores = autorService.listar(nome, nacionalidade);

        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            AutorRequestDTO dto) {

        AutorResponseDTO autor = autorService.atualizar(id, dto);
        return ResponseEntity.ok(autor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        autorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
