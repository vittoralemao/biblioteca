-- V1: schema inicial da API Biblioteca
-- Reflete o banco criado pelo Hibernate (ddl-auto) até a versão 1.1.0.
-- Os nomes das constraints foram mantidos iguais aos de produção.

CREATE TABLE nacionalidades (
                                id uuid NOT NULL,
                                iso character varying(3) NOT NULL,
                                nome character varying(60) NOT NULL,
                                usuario_cadastro character varying(255),
                                usuario_ultima_atualizacao character varying(255),
                                data_cadastro timestamp(6) without time zone NOT NULL,
                                data_atualizacao timestamp(6) without time zone NOT NULL,
                                CONSTRAINT nacionalidades_pkey PRIMARY KEY (id),
                                CONSTRAINT ukc48uu4umtkt74x28hdcs1bpyt UNIQUE (nome),
                                CONSTRAINT ukta388w8nrg81xjujj2uwrb2vx UNIQUE (iso)
);

CREATE TABLE categorias (
                            id uuid NOT NULL,
                            nome character varying(25) NOT NULL,
                            usuario_cadastro character varying(255),
                            usuario_ultima_atualizacao character varying(255),
                            data_cadastro timestamp(6) without time zone NOT NULL,
                            data_atualizacao timestamp(6) without time zone NOT NULL,
                            CONSTRAINT categorias_pkey PRIMARY KEY (id),
                            CONSTRAINT uk9qte5svl2i6n82lpdyyheoi1h UNIQUE (nome)
);

CREATE TABLE autores (
                         id uuid NOT NULL,
                         data_atualizacao timestamp(6) without time zone NOT NULL,
                         data_cadastro timestamp(6) without time zone NOT NULL,
                         usuario_ultima_atualizacao character varying(255),
                         data_nascimento date NOT NULL,
                         nome character varying(100) NOT NULL,
                         nacionalidade_id uuid NOT NULL,
                         usuario_cadastro character varying(255),
                         CONSTRAINT autores_pkey PRIMARY KEY (id),
                         CONSTRAINT fkhijv73f4n5a1109950el8f33s FOREIGN KEY (nacionalidade_id) REFERENCES nacionalidades (id)
);

CREATE TABLE livros (
                        id uuid NOT NULL,
                        data_atualizacao timestamp(6) without time zone NOT NULL,
                        data_cadastro timestamp(6) without time zone NOT NULL,
                        usuario_ultima_atualizacao character varying(255),
                        ano_publicacao integer,
                        isbn character varying(20),
                        titulo character varying(150) NOT NULL,
                        autor_id uuid NOT NULL,
                        categoria_id uuid NOT NULL,
                        usuario_cadastro character varying(255),
                        CONSTRAINT livros_pkey PRIMARY KEY (id),
                        CONSTRAINT ukbm02y40a3t3ambvf91804i3kv UNIQUE (isbn),
                        CONSTRAINT fkmjvs91l0cqtg1hy9kfj3b40fy FOREIGN KEY (autor_id) REFERENCES autores (id),
                        CONSTRAINT fkcnit3grh7fp4nlc97c878rvwh FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);

CREATE TABLE usuario (
                         id uuid NOT NULL,
                         data_atualizacao timestamp(6) without time zone NOT NULL,
                         data_cadastro timestamp(6) without time zone NOT NULL,
                         usuario_ultima_atualizacao character varying(255),
                         login character varying(255) NOT NULL,
                         nome character varying(255) NOT NULL,
                         papel character varying(255) NOT NULL,
                         senha character varying(255) NOT NULL,
                         usuario_cadastro character varying(255),
                         CONSTRAINT usuario_pkey PRIMARY KEY (id),
                         CONSTRAINT ukpm3f4m4fqv89oeeeac4tbe2f4 UNIQUE (login),
                         CONSTRAINT usuario_papel_check CHECK (papel IN ('GERENTE', 'OPERADOR'))
);