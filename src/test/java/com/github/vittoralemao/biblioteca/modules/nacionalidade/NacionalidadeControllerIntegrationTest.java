package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import com.github.vittoralemao.biblioteca.TestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "jwt.secret=chave-somente-para-testes-com-pelo-menos-32-bytes")
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
public class NacionalidadeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NacionalidadeRepository nacionalidadeRepository;

    private void salvarNacionalidade(String nome, String iso) {
        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setNome(nome);
        nacionalidade.setIso(iso);
        nacionalidadeRepository.save(nacionalidade);
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveListarTodasAsNacionalidadesQuandoNenhumFiltroForInformado() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get("/nacionalidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorNomeIgnorandoMaiusculas() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get("/nacionalidades").param("nome", "iNgLe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("INGLESA"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorIsoIgnorandoMaiusculas() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get("/nacionalidades").param("iso", "gb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].iso").value("GBR"));
    }
}
