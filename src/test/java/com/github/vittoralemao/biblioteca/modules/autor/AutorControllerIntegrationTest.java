package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.TestContainerConfiguration;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "jwt.secret=chave-somente-para-testes-com-pelo-menos-32-bytes")
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
public class AutorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private NacionalidadeRepository nacionalidadeRepository;

    private UUID brasileiraId;
    private UUID inglesaId;

    private void salvarAutor(String nome, UUID nacionalidadeId){
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidadeRepository.getReferenceById(nacionalidadeId));
        autor.setDataNascimento(LocalDate.of(1900,10,10));
        autorRepository.save(autor);
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveListarTodosOsAutoresQuandoNenhumFiltroForInformado() throws Exception{
        salvarAutor("Machado de Assis", brasileiraId);
        salvarAutor("George Orwell", inglesaId);

        mockMvc.perform(get("/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorNomeIgnorandoMaiusculas() throws Exception{
        salvarAutor("Machado de Assis", brasileiraId);
        salvarAutor("George Orwell", inglesaId);

        mockMvc.perform(get("/autores").param("nome", "mac"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Machado de Assis"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveCombinarFiltroDeNomeENacionalidade() throws Exception{
        salvarAutor("Machado de Assis", brasileiraId);
        salvarAutor("George Orwell", inglesaId);

        mockMvc.perform(get("/autores")
                        .param("nome", "mac")
                        .param("nacionalidade", "bra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/autores")
                        .param("nome", "mac")
                        .param("nacionalidade", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPeloNomeDaNacionalidade() throws Exception {
        salvarAutor("Machado de Assis", brasileiraId);
        salvarAutor("George Orwell", inglesaId);

        mockMvc.perform(get("/autores").param("nacionalidade", "ingl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("George Orwell"));
    }

    @BeforeEach
    void setUp(){
        Nacionalidade brasileira = new Nacionalidade();
        brasileira.setNome("BRASILEIRA");
        brasileira.setIso("BRA");

        brasileiraId = nacionalidadeRepository.save(brasileira).getId();

        Nacionalidade inglesa = new Nacionalidade();
        inglesa.setNome("INGLESA");
        inglesa.setIso("GBR");

        inglesaId = nacionalidadeRepository.save(inglesa).getId();
    }
}
