package com.github.vittoralemao.biblioteca.modules.livro;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Livros", description = "Gerenciamento de livros")
@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PreAuthorize("hasRole('GERENTE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Livro duplicado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable UUID id){
        LivroResponseDTO livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
    @GetMapping
    public ResponseEntity<PagedModel<LivroResponseDTO>> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria,
            @PageableDefault(size = 20, sort = "titulo") Pageable pageable
            ) {
        Page<LivroResponseDTO> livros = livroService.listar(titulo, autor, categoria, pageable);
        return ResponseEntity.ok(new PagedModel<>(livros));
    }

    @PreAuthorize("hasRole('GERENTE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Livro duplicado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            LivroRequestDTO dto
    ) {
        LivroResponseDTO livro = livroService.atualizar(id, dto);
        return ResponseEntity.ok(livro);
    }

    @PreAuthorize("hasRole('GERENTE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
