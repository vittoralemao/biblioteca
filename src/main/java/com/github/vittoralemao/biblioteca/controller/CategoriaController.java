package com.github.vittoralemao.biblioteca.controller;

import com.github.vittoralemao.biblioteca.dto.CategoriaRequestDTO;
import com.github.vittoralemao.biblioteca.dto.CategoriaResponseDTO;
import com.github.vittoralemao.biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrar(@RequestBody @Valid CategoriaRequestDTO dto){
        CategoriaResponseDTO categoriaCriada = categoriaService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaCriada.id())
                .toUri();

        return ResponseEntity.created(location).body(categoriaCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            CategoriaRequestDTO dto
    ){
        CategoriaResponseDTO categoria = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable UUID id){
        CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(
            @RequestParam(required = false) String nome
    ){
        List<CategoriaResponseDTO> categorias = categoriaService.listar(nome);

        return ResponseEntity.ok(categorias);
    }

}
