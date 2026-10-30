package com.github.vittoralemao.biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestContainerConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class BibliotecaApplicationTests {

	@Test
	void contextLoads() {
	}

}
