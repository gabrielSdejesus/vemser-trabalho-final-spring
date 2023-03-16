-- 	CREATE USER --

--CREATE USER AVIACAO IDENTIFIED BY oracle;
--GRANT CONNECT TO AVIACAO;
--GRANT CONNECT, RESOURCE, DBA TO VEM_SER;
--GRANT CREATE SESSION TO AVIACAO;
--GRANT DBA TO AVIACAO;
--GRANT CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE TO AVIACAO;
--GRANT CREATE MATERIALIZED VIEW TO AVIACAO;
--GRANT GLOBAL QUERY REWRITE TO AVIACAO;
--GRANT SELECT ANY TABLE TO AVIACAO;

-- CLEAN EVERYTHING IF NEEDED --
--DROP TABLE AVIACAO.PASSAGEM;
--DROP TABLE AVIACAO.VENDA;
--DROP TABLE AVIACAO.TRECHO;
--DROP TABLE AVIACAO.COMPANHIA;
--DROP TABLE AVIACAO.COMPRADOR;
--DROP TABLE AVIACAO.USUARIO;
--DROP SEQUENCE AVIACAO.seq_usuario;
--DROP SEQUENCE AVIACAO.seq_companhia;
--DROP SEQUENCE AVIACAO.seq_comprador;
--DROP SEQUENCE AVIACAO.seq_passagem;
--DROP SEQUENCE AVIACAO.seq_trecho;
--DROP SEQUENCE AVIACAO.seq_venda;

 -- CREATE TABLES --

CREATE TABLE AVIACAO.USUARIO (
  id_usuario NUMBER,
  login VARCHAR2(100) UNIQUE NOT NULL,
  senha VARCHAR2(20) NOT NULL,
  nome VARCHAR2(100) NOT NULL,
  tipo NUMBER(1) NOT NULL,
  ativo NUMBER(1) NOT NULL,
  PRIMARY KEY (id_usuario)
);

CREATE TABLE AVIACAO.COMPRADOR (
  id_comprador NUMBER,
  cpf CHAR(14) UNIQUE NOT NULL,
  id_usuario NUMBER UNIQUE NOT NULL,
  PRIMARY KEY (id_comprador),
  CONSTRAINT FK_COMPRADOR_ID_USUARIO FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE AVIACAO.COMPANHIA (
  id_companhia NUMBER,
  cnpj CHAR(18) UNIQUE NOT NULL,
  nome_fantasia VARCHAR2(100) NOT NULL,
  id_usuario NUMBER UNIQUE NOT NULL,
  PRIMARY KEY (id_companhia),
  CONSTRAINT FK_COMPANHIA_ID_USUARIO FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE AVIACAO.VENDA (
  id_venda NUMBER,
  codigo VARCHAR2(36) UNIQUE NOT NULL,
  status VARCHAR2(20) NOT NULL,
  data_venda TIMESTAMP NOT NULL,
  id_companhia NUMBER NOT NULL,
  id_comprador NUMBER NOT NULL,
  PRIMARY KEY (id_venda),
  CONSTRAINT FK_VENDA_ID_COMPRADOR FOREIGN KEY (id_comprador) REFERENCES COMPRADOR(id_comprador),
  CONSTRAINT FK_VENDA_ID_COMPANHIA FOREIGN KEY (id_companhia) REFERENCES COMPANHIA(id_companhia)
);

CREATE TABLE AVIACAO.TRECHO (
  id_trecho NUMBER,
  origem CHAR(3) NOT NULL,
  destino CHAR(3) NOT NULL,
  id_companhia NUMBER NOT NULL,
  PRIMARY KEY (id_trecho),
  CONSTRAINT FK_TRECHO_ID_COMPANHIA FOREIGN KEY (id_companhia) REFERENCES COMPANHIA(id_companhia)
);

CREATE TABLE AVIACAO.PASSAGEM (
  id_passagem NUMBER,
  codigo VARCHAR2(36) UNIQUE NOT NULL,
  data_partida TIMESTAMP NOT NULL,
  data_chegada TIMESTAMP NOT NULL,
  disponivel CHAR(1) NOT NULL,
  valor NUMBER(19,4) NOT NULL,
  id_trecho NUMBER NOT NULL,
  id_venda NUMBER,
  PRIMARY KEY (id_passagem),
  CONSTRAINT FK_PASSAGEM_ID_VENDA FOREIGN KEY (id_venda) REFERENCES VENDA(id_venda),
  CONSTRAINT FK_PASSAGEM_ID_TRECHO FOREIGN KEY (id_trecho) REFERENCES TRECHO(id_trecho)
);

-- SEQUENCES --

CREATE SEQUENCE AVIACAO.seq_usuario
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE AVIACAO.seq_companhia
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE AVIACAO.seq_comprador
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE AVIACAO.seq_passagem
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE AVIACAO.seq_trecho
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE AVIACAO.seq_venda
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;