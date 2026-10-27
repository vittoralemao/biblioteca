ALTER TABLE categorias     RENAME CONSTRAINT uk9qte5svl2i6n82lpdyyheoi1h TO uk_categorias_nome;
ALTER TABLE nacionalidades RENAME CONSTRAINT ukc48uu4umtkt74x28hdcs1bpyt TO uk_nacionalidades_nome;
ALTER TABLE nacionalidades RENAME CONSTRAINT ukta388w8nrg81xjujj2uwrb2vx TO uk_nacionalidades_iso;
ALTER TABLE livros         RENAME CONSTRAINT ukbm02y40a3t3ambvf91804i3kv TO uk_livros_isbn;
ALTER TABLE usuario        RENAME CONSTRAINT ukpm3f4m4fqv89oeeeac4tbe2f4 TO uk_usuario_login;
ALTER TABLE livros         RENAME CONSTRAINT fkmjvs91l0cqtg1hy9kfj3b40fy TO fk_livros_autor;
ALTER TABLE livros         RENAME CONSTRAINT fkcnit3grh7fp4nlc97c878rvwh TO fk_livros_categoria;
ALTER TABLE autores        RENAME CONSTRAINT fkhijv73f4n5a1109950el8f33s TO fk_autores_nacionalidade;