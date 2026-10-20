package com.github.vittoralemao.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "jwt.secret=chave-somente-para-testes-com-pelo-menos-32-bytes")
class BibliotecaApplicationTests {

	@Test
	void contextLoads() {
	}

}
