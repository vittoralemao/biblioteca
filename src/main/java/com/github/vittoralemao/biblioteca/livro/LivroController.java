package com.github.vittoralemao.biblioteca.livro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(@RequestBody @Valid LivroRequestDTO dto){
        LivroResponseDTO livroCriado = livroService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(livroCriado.id())
                .toUri();

        return ResponseEntity.created(location).body(livroCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable UUID id){
        LivroResponseDTO livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria
    ) {
        List<LivroResponseDTO> livros = livroService.listar(titulo, autor, categoria);
        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            LivroRequestDTO dto
    ) {
        LivroResponseDTO livro = livroService.atualizar(id, dto);
        return ResponseEntity.ok(livro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
