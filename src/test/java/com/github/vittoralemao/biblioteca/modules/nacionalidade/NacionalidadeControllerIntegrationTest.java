package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import com.github.vittoralemao.biblioteca.TestContainerConfiguration;
import com.github.vittoralemao.biblioteca.config.ApiVersionConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
public class NacionalidadeControllerIntegrationTest {

    private static final String URL = ApiVersionConfig.PREFIXO_V1 + "/nacionalidades";

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

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorNomeIgnorandoMaiusculas() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get(URL).param("nome", "iNgLe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("INGLESA"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorIsoIgnorandoMaiusculas() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get(URL).param("iso", "gb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].iso").value("GBR"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveCombinarFiltrosDeNomeEIso() throws Exception {
        salvarNacionalidade("BRASILEIRA", "BRA");
        salvarNacionalidade("INGLESA", "GBR");

        mockMvc.perform(get(URL).param("nome", "ingl").param("iso", "gbr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        mockMvc.perform(get(URL).param("nome", "ingl").param("iso", "bra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
