package com.github.vittoralemao.biblioteca.nacionalidade;

import com.github.vittoralemao.biblioteca.share.ErroResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Nacionalidades", description = "Gerenciamento de nacionalidades")
@RestController
@RequestMapping("/nacionalidades")
@RequiredArgsConstructor
public class NacionalidadeController {

    private final NacionalidadeService nacionalidadeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nacionalidade cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Nacionalidade duplicada",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
    })
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nacionalidade encontrada"),
            @ApiResponse(responseCode = "404", description = "Nacionalidade não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> buscarPorId(@PathVariable UUID id){
        NacionalidadeResponseDTO nacionalidade = nacionalidadeService.buscarPorId(id);
        return ResponseEntity.ok(nacionalidade);
    }

    @ApiResponse(responseCode = "200", description = "Lista de nacionalidades retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<NacionalidadeResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String iso
    ) {

        List<NacionalidadeResponseDTO> nacionalidades = nacionalidadeService.listar(nome, iso);

        return ResponseEntity.ok(nacionalidades);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nacionalidade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nacionalidade não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Nacionalidade duplicada",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid
            NacionalidadeRequestDTO dto
    ) {
        NacionalidadeResponseDTO nacionalidade = nacionalidadeService.atualizar(id, dto);
        return ResponseEntity.ok(nacionalidade);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nacionalidade excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nacionalidade não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Nacionalidade possui autor vinculado",
                    content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        nacionalidadeService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
