package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.TestContainerConfiguration;
import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaRepository;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@Transactional
public class LivroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private NacionalidadeRepository nacionalidadeRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private UUID autorId;
    private UUID categoriaId;

    private void salvarLivro(String titulo) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autorRepository.getReferenceById(autorId));
        livro.setCategoria(categoriaRepository.getReferenceById(categoriaId));
        livroRepository.save(livro);
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveRetornarProblemDetailsQuandoOLivroNaoExiste() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(get("/livros/" + idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.type")
                        .value("https://biblioteca.vitoralemao.com.br/problemas/recurso-nao-encontrado"))
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(containsString(idInexistente.toString())))
                .andExpect(jsonPath("$.instance").value("/livros/" + idInexistente));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveRetornarProblemDetailsQuandoARotaNaoExiste() throws Exception {
        mockMvc.perform(get("/livro"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.type")
                        .value("https://biblioteca.vitoralemao.com.br/problemas/recurso-nao-encontrado"))
                .andExpect(jsonPath("$.detail").value("Rota não encontrada: /livro"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveListarTodosOsLivrosQuandoNenhumFiltroForInformado() throws Exception {
        salvarLivro("1984");
        salvarLivro("Dom Casmurro");

        mockMvc.perform(get("/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveFiltrarPorTituloIgnorandoMaiusculas() throws Exception {
        salvarLivro("1984");
        salvarLivro("Dom Casmurro");

        mockMvc.perform(get("/livros").param("titulo", "dom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Dom Casmurro"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void deveCombinarFiltrosDeAutorECategoria() throws Exception {
        salvarLivro("1984");

        mockMvc.perform(get("/livros").param("autor", "aut").param("categoria", "fic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        mockMvc.perform(get("/livros").param("autor", "aut").param("categoria", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    void devePaginarEOrdenarPorTitulo() throws Exception {
        salvarLivro("C");
        salvarLivro("A");
        salvarLivro("B");

        mockMvc.perform(get("/livros").param("size", "2").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("C"))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornar400QuandoOCampoDeOrdenacaoNaoExiste() throws Exception{
        mockMvc.perform(get("/livros").param("sort", "campoQueNaoExiste"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornar400QuandoOIsbnEInvalido() throws Exception {
        mockMvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                "titulo": "1984",
                                "autorId": "%s",
                                "categoriaId": "%s",
                                "isbn": "978-85-359-1484-0",
                                "anoPublicacao": 1949
                                }
                                """.formatted(autorId, categoriaId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveCadastrarLivroERetornar201() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "titulo": "1984",
                                "autorId": "%s",
                                "categoriaId": "%s",
                                "isbn": "9788535914849",
                                "anoPublicacao": 1949
                                }
                                """.formatted(autorId, categoriaId)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.titulo").value("1984"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornar404QuandoOAutorNaoExiste() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "titulo": "1984",
                                "autorId": "%s",
                                "categoriaId": "%s",
                                "isbn": "9788535914849",
                                "anoPublicacao": 1949
                                }
                                """.formatted(UUID.randomUUID(), categoriaId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornar409QuandoOIsbnJaExiste() throws Exception {

        String livroJson = """
                {
                "titulo": "1984",
                "autorId": "%s",
                "categoriaId": "%s",
                "isbn": "9788535914849",
                "anoPublicacao": 1949
                }
                """.formatted(autorId, categoriaId);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRetornar400QuandoOAutorNaoEInformado() throws Exception {
        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "titulo": "1984",
                                "categoriaId": "%s",
                                "isbn": "9788535914849",
                                "anoPublicacao": 1949
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @BeforeEach
    void setUp() {
        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");

        nacionalidade = nacionalidadeRepository.save(nacionalidade);

        Autor autor = new Autor();
        autor.setNome("AUTOR");
        autor.setNacionalidade(nacionalidade);
        autor.setDataNascimento(LocalDate.of(2004, 12, 4));

        autorId = autorRepository.save(autor).getId();

        Categoria categoria = new Categoria();
        categoria.setNome("FICCAO");

        categoriaId = categoriaRepository.save(categoria).getId();
    }
}
