package com.github.vittoralemao.biblioteca.modules.usuario;

import com.github.vittoralemao.biblioteca.TestContainerConfiguration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "jwt.secret=chave-somente-para-testes-com-pelo-menos-32-bytes")
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
public class AuthControllerIntegrationTest {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarTokenComCredenciaisValidas() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "login":"teste@teste.com",
                                "senha":"senhateste"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void naoDeveAutenticarComSenhaErrada() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "login":"teste@teste.com",
                                "senha":"senhaerrada"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Login ou senha inválidos."));
    }

    @Test
    void naoDeveAutenticarComLoginInexistente() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "login":"ninguem@teste.com",
                                "senha":"senhateste"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Login ou senha inválidos."));
    }

    @Test
    void deveAcessarEndpointProtegidoComTokenGerado() throws Exception {
        String json = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "login":"teste@teste.com",
                                "senha":"senhateste"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = JsonPath.read(json, "$.token");

        mockMvc.perform(get("/categorias")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @BeforeEach
    void setUp() {
        usuarioRepository.save(new Usuario(
                "TESTE",
                "teste@teste.com",
                passwordEncoder.encode("senhateste"),
                Papel.OPERADOR));
    }
}
