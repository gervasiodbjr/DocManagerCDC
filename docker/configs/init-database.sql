-- Dando Permissão de Replicação para o Usuário 'admin'
ALTER USER admin WITH REPLICATION;

-- 1. Tabela de Departamento
CREATE TABLE IF NOT EXISTS departamento (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(128) NOT NULL UNIQUE
);

-- 2. Tabela de Categoria
CREATE TABLE IF NOT EXISTS categoria (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(128) NOT NULL UNIQUE
);

-- 3. Tabela de Tag
CREATE TABLE IF NOT EXISTS tag (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(128) NOT NULL UNIQUE
);

-- 4. Tabela de Documento
CREATE TABLE IF NOT EXISTS documento (
  id BIGSERIAL PRIMARY KEY,
  titulo VARCHAR(256) NOT NULL,
  corpo TEXT NOT NULL,
  criado_em TIMESTAMPTZ DEFAULT NOW() NOT NULL,
  atualizado_em TIMESTAMPTZ DEFAULT NOW() NOT NULL,
  departamento_id INT NOT NULL REFERENCES departamento(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_documento_departamento_id ON documento(departamento_id);

-- 5. Associação muitos-para-muitos: documento <-> categoria
CREATE TABLE IF NOT EXISTS doc_categoria (
  doc_id INT NOT NULL REFERENCES documento(id) ON DELETE CASCADE,
  categoria_id INT NOT NULL REFERENCES categoria(id) ON DELETE CASCADE,
  PRIMARY KEY (doc_id, categoria_id)
);

CREATE INDEX IF NOT EXISTS idx_doc_categoria_categoria_id ON doc_categoria(categoria_id);

-- 6. Associação muitos-para-muitos: documento <-> tag
CREATE TABLE IF NOT EXISTS doc_tag (
  doc_id INT NOT NULL REFERENCES documento(id) ON DELETE CASCADE,
  tag_id INT NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
  PRIMARY KEY (doc_id, tag_id)
);

CREATE INDEX IF NOT EXISTS idx_doc_tag_tag_id ON doc_tag(tag_id);

ALTER TABLE departamento REPLICA IDENTITY FULL;
ALTER TABLE categoria REPLICA IDENTITY FULL;
ALTER TABLE tag REPLICA IDENTITY FULL;
ALTER TABLE documento REPLICA IDENTITY FULL;
ALTER TABLE doc_categoria REPLICA IDENTITY FULL;
ALTER TABLE doc_tag REPLICA IDENTITY FULL;

-- VIEW substituindo as chaves estrangeiras pelos valores descritivos
CREATE OR REPLACE VIEW documento_view AS
SELECT
  d.id,
  d.titulo,
  d.corpo,
  d.criado_em,
  d.atualizado_em,
  dep.nome AS departamento,
  ARRAY(
    SELECT c.nome
    FROM doc_categoria dc
    JOIN categoria c ON dc.categoria_id = c.id
    WHERE dc.doc_id = d.id
    ORDER BY c.nome
  ) AS categorias,
  ARRAY(
    SELECT t.nome
    FROM doc_tag dt
    JOIN tag t ON dt.tag_id = t.id
    WHERE dt.doc_id = d.id
    ORDER BY t.nome
  ) AS tags
FROM documento d
JOIN departamento dep ON d.departamento_id = dep.id;

-- INSERINDO DADOS INICIAIS DE TESTE

INSERT INTO "tag" ("id", "nome") VALUES
(1,	'Tag 01'),
(2,	'Tag 02');

INSERT INTO "categoria" ("id", "nome") VALUES
(1,	'Categoria 01'),
(2,	'Categoria 02');

INSERT INTO "departamento" ("id", "nome") VALUES
(1,	'Departamento 01'),
(2,	'Departamento 02');

INSERT INTO "documento" ("titulo", "corpo", "departamento_id") VALUES
('Documento 01', 'Conteúdo do documento de teste 01', 1);

INSERT INTO "doc_categoria" ("doc_id", "categoria_id") VALUES
(1,	1);

INSERT INTO "doc_tag" ("doc_id", "tag_id") VALUES
(1,	1);