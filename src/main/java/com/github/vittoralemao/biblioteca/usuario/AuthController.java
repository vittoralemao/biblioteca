package com.github.vittoralemao.biblioteca.usuario;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@Valid @RequestBody LoginRequestDTO request){
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
