package com.github.vittoralemao.biblioteca.nacionalidade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nacionalidades")
@RequiredArgsConstructor
public class NacionalidadeController {

    private final NacionalidadeService nacionalidadeService;

    @PostMapping
    public ResponseEntity<NacionalidadeResponseDTO> cadastrar (@RequestBody @Valid NacionalidadeRequestDTO dto){
        NacionalidadeResponseDTO nacionalidadeCriada = nacionalidadeService.cadastrar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nacionalidadeCriada.id())
                .toUri();

        return ResponseEntity.created(location).body(nacionalidadeCriada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            NacionalidadeRequestDTO dto
    ) {
        NacionalidadeResponseDTO nacionalidade = nacionalidadeService.atualizar(id, dto);
        return ResponseEntity.ok(nacionalidade);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> buscarPorId(@PathVariable UUID id){
        NacionalidadeResponseDTO nacionalidade = nacionalidadeService.buscarPorId(id);
        return ResponseEntity.ok(nacionalidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        nacionalidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NacionalidadeResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String iso
    ) {

        List<NacionalidadeResponseDTO> nacionalidades = nacionalidadeService.listar(nome, iso);

        return ResponseEntity.ok(nacionalidades);
    }
}
